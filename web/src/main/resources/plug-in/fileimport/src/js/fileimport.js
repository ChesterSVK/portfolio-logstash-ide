/**
 *    Rich Filemanager JS core
 *
 *    filemanager.js
 *
 *    @license    MIT License
 *    @author        Jason Huck - Core Five Labs
 *    @author        Simon Georget <simon (at) linea21 (dot) com>
 *    @author        Pavel Solomienko <https://github.com/servocoder/>
 *    @copyright    Authors
 */

(function ($) {

    $.fileImportPlugin = function (element, pluginOptions) {
        /**
         * Plugin's default options
         */
        var defaults = {
            baseUrl: '/plug-in/fileimport',	// relative path to the FM plugin folder
            libsUrl: '/libs',
            configUrl: null,
            config: {} // configuration options
        };

        /**
         * The reference the current instance of the object
         */
        var fi = this;

        /**
         * Private properties accessible only from inside the plugin
         */
        var $container = $(element),	// reference to the jQuery version of DOM element the plugin is attached to
            // $controlsColumn = $container.find('.controls-column'),
            $controls = $container.find('#importControls'),
            $picker = $container.find('#importPicker'),
            $controlsWrapper = $controls.children('.fi-wrapper'),
            $pickerWrapper = $picker.children('.fi-wrapper'),
            // $pickerHeader = $pickerWrapper.children('.fi-header'),
            // $pickerHolder = $pickerWrapper.children('.fi-holder'),
            $filemanagerItem = null,


            config = null,				// configuration options
            apiConnector = null,		// API connector URL to perform requests to server
            configSortField = 'name',		// items sort field name
            configSortOrder = 'asc',		// items sort order 'asc'/'desc'
            fiModel = null,				// filemanager knockoutJS model
            langModel = null,			// language model
            globalize = null,			// formatting and parsing tool (numbers, dates, etc.)
            delayStack = null,
            $fileManagerId = 'fileManager',// function execution delay manager,
            initialData = [],
            token = $("meta[name='_csrf']").attr("content"),


            /** service variables **/
            _url_ = purl(),
            timeStart = new Date().getTime();

        /**
         * This holds the merged default and user-provided options.
         * Plugin's properties will be available through this object like:
         * - fi.propertyName from inside the plugin
         * - element.host('richFilemanager').propertyName from outside the plugin, where "element" is the element the plugin is attached to;
         * @type {{}}
         */

        // The plugin's final settings, contains the merged default and user-provided options (if any)
        fi.settings = $.extend(true, defaults, pluginOptions);


        /*--------------------------------------------------------------------------------------------------------------
	 Public methods
	 Can be called like:
	 - fi.methodName(arg1, arg2, ... argn) from inside the plugin
	 - element.host('richFilemanager').publicMethod(arg1, arg2, ... argn) from outside the plugin,
	   where "element" is the element the plugin is attached to
	--------------------------------------------------------------------------------------------------------------*/

        fi.write = function (message, obj) {
            var log = alertify;
            var options = $.extend({}, {
                reset: true,
                delay: 5000,
                logMaxItems: 5,
                logPosition: 'bottom right',
                logContainerClass: 'fi-log',
                logMessageTemplate: null,
                parent: document.body,
                onClick: undefined,
                unique: false,
                type: 'log'
            }, obj);

            // display only one log for the specified 'logClass'
            if (options.logClass && options.unique && $('.fi-log').children('.' + options.logClass).length > 0) {
                return log;
            }

            if (options.reset) log.reset();
            log.parent(options.parent);
            log.logDelay(options.delay);
            log.logMaxItems(options.logMaxItems);
            log.logPosition(options.logPosition);
            log.logContainerClass(options.logContainerClass);
            log.logMessageTemplate(options.logMessageTemplate);
            log[options.type](message, options.onClick);

            var logs = log.getLogs();
            return logs[logs.length - 1];
        };

        fi.error = function (message, options) {
            return fi.write(message, $.extend({}, {
                type: 'error',
                delay: 10000
            }, options));
        };

        fi.warning = function (message, options) {
            return fi.write(message, $.extend({}, {
                type: 'warning',
                delay: 10000
            }, options));
        };

        fi.success = function (message, options) {
            return fi.write(message, $.extend({}, {
                type: 'success',
                delay: 6000
            }, options));
        };

        fi.alert = function (message) {
            alertify
                .reset()
                .dialogContainerClass('fi-popup')
                .alert(message);
        };

        fi.confirm = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fi-popup')
                .confirm(obj.message, obj.okBtn, obj.cancelBtn);
        };

        fi.prompt = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fi-popup')
                .theme(obj.template)
                .prompt(obj.message, obj.value || '', obj.okBtn, obj.cancelBtn);
        };

        fi.dialog = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fi-popup')
                .dialog(obj.message, obj.buttons);
        };


        /*--------------------------------------------------------------------------------------------------------------
	 Private methods
	 These methods can be called only from inside the plugin like: methodName(arg1, arg2, ... argn)
	--------------------------------------------------------------------------------------------------------------*/

        /**
         * The "constructor" method that gets called when the object is created
         */
        var construct = function () {
            var deferred = $.Deferred();

            deferred
                .then(function () {
                    return configure();
                })
                .then(function () {
                    return localize();
                })
                .then(function () {
                    return performInitialRequest();
                })
                .then(function () {
                    includeAssets(function () {
                        initialize();
                    });
                });

            deferred.resolve();
        };

        var configure = function () {
            return $.when(loadConfigFile('default'), loadConfigFile('user')).done(function (confd, confu) {
                var config_default = confd[0];
                var config_user = confu[0];

                // remove version from user config file
                if (config_user !== undefined && config_user !== null) {
                    delete config_user.version;
                }
                // merge default config and user config file
                config = $.extend({}, config_default, config_user);

                // setup apiConnector
                if (config.api.connectorUrl) {
                    apiConnector = config.api.connectorUrl;
                }
            });
        };

        // performs initial request to server to retrieve initial params
        var performInitialRequest = function () {
            return buildAjaxRequest('GET', {
            }).done(function (response) {
                if (response.data) {
                    initialData = response.data;
                }
            }).fail(function (xhr) {
                fi.error('Unable to perform initial request to server.');
                handleAjaxError(xhr);
            }).then(function (response) {
                if (response.errors) {
                    return $.Deferred().reject();
                }
            });
        };

        // Handle ajax request errors.
        var handleAjaxError = function (xhr) {
            var errorMessage;

            if ($.isPlainObject(xhr) && xhr.responseText) {
                var isJSON = (xhr.getResponseHeader('content-type') === 'application/json');

                // on "readfile" API request (dataType === 'text')
                if (!xhr.responseJSON && isJSON) {
                    xhr.responseJSON = $.parseJSON(xhr.responseText);
                }

                if ($.isPlainObject(xhr.responseJSON) && xhr.responseJSON.errors) {
                    handleJsonErrors(xhr.responseJSON.errors);
                } else {
                    errorMessage = 'Error' + ' ' + xhr.responseText;
                }
            } else {
                // $.Deferred().reject() case e.g.
                errorMessage = xhr;
            }

            if (errorMessage) {
                // fi.console('ERROR TEXT', errorMessage);
                fi.error(errorMessage);
            }
        };

        // Handle JSON server errors.
        var handleJsonErrors = function (errors) {
            fi.console('ERROR JSON', errors);

            $.each(errors, function (i, errorObject) {
                fi.error(formatServerError(errorObject));

                if (errorObject.meta.redirect) {
                    window.location.href = errorObject.meta.redirect;
                }
            });
        };


        // localize messages based on configuration or URL value
        var localize = function () {
            langModel = new LangModel();

            return $.ajax()
                .then(function () {
                    var urlLangCode = config.language.default;
                    if ($('meta[name=langCode]') !== undefined){
                        var metaLangCode = $('meta[name=langCode]').attr('content');
                        if (config.language.available.indexOf(metaLangCode) !== -1){
                            urlLangCode = $('meta[name=langCode]').attr('content');
                        }
                    }
                    else {
                        urlLangCode = _url_.param('langCode');
                    }
                    if (urlLangCode) {
                        // try to load lang file based on langCode in query params
                        return file_exists(langModel.buildLangFileUrl(urlLangCode))
                            .done(function () {
                                langModel.setLang(urlLangCode);
                            })
                            .fail(function () {
                                setTimeout(function () {
                                    fi.error('Given language file (' + langModel.buildLangFileUrl(urlLangCode) + ') does not exist!');
                                }, 500);
                            });
                    } else {
                        langModel.setLang(urlLangCode);
                    }
                })
                .then(function () {
                    // append query param to prevent caching
                    var langFileUrl = langModel.buildLangFileUrl(langModel.getLang()) + '?_=' + new Date().getTime();

                    return $.ajax({
                        type: 'GET',
                        url: langFileUrl,
                        dataType: 'json'
                    }).done(function (jsonTrans) {
                        langModel.setTranslations(jsonTrans);
                    });
                })
                .then(function () {
                    // trim language code to first 2 chars
                    var lang = langModel.getLang().substr(0, 2),
                        baseUrl = fi.settings.baseUrl,
                        libsUrl = fi.settings.libsUrl;

                    return $.when(
                        $.get(libsUrl + '/cldrjs/cldr-dates/' + lang + '/ca-gregorian.json'),
                        $.get(libsUrl + '/cldrjs/cldr-numbers/' + lang + '/numbers.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/likelySubtags.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/timeData.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/weekData.json')
                    ).fail(function () {
                        fi.error('CLDR files for "' + lang + '" language do not exist!');
                    }).then(function () {
                        // Normalize $.get results, we only need the JSON, not the request statuses.
                        return [].slice.apply(arguments, [0]).map(function (result) {
                            return result[0];
                        });
                    }).then(Globalize.load).then(function () {
                        globalize = Globalize(lang);
                    });
                });
        };

        var includeAssets = function (callback) {
            var primary = [];

            // theme defined in configuration file
            // primary.push('/themes/' + config.options.theme + '/styles/theme.css');

            // add callback on loaded assets and inject primary ones
            primary.push(callback);
            loadAssets(primary);
        };


        var initialize = function () {
            delayStack = new DelayStack();

            // Activates knockout.js
            fiModel = new ImportModel();
            if (!isKnockoutBounded(element)) {
                ko.applyBindings(fiModel, element);
            }
            // remove loading screen div
            fiModel.loadingView(false);
        };

        /**
         * Language model
         *
         * @constructor
         */
        var LangModel = function () {
            var currentLang = "cs",
                translationsHash = {},
                translationsPath = fi.settings.baseUrl + '/languages/';

            this.buildLangFileUrl = function (code) {
                return translationsPath + code + '.json';
            };

            this.setLang = function (code) {
                currentLang = code;
            };

            this.getLang = function () {
                return currentLang;
            };

            this.setTranslations = function (json) {
                translationsHash = json;
            };

            this.getTranslations = function () {
                return translationsHash;
            };

            this.translate = function (key) {
                return translationsHash[key];
            };
        };

        /**
         * DelayStack
         * Delays execution of functions that is passed as argument
         *
         * @constructor
         */
        var DelayStack = function () {
            var hash = {},
                delay_stack = this;

            this.push = function (name, callback, ms) {
                delay_stack.removeTimer(name);
                hash[name] = setTimeout(callback, ms);
            };

            this.getTimer = function (name) {
                return hash[name];
            };

            this.removeTimer = function (name) {
                if (hash[name]) {
                    clearTimeout(hash[name]);
                    delete hash[name];
                }
            };
        };

        /**
         * Knockout general model
         *
         * @constructor
         */
        var ImportModel = function () {
            var model = this;
            this.config = ko.observable(config);
            this.loadingView = ko.observable(true);
            this.currentLang = langModel.getLang();
            this.lg = langModel.getTranslations();

            var ControlPanelModel = function () {
                var panel_model = this;

                this.importItems = function () {
                    $filemanagerItem = ko.dataFor(document.getElementById($fileManagerId));
                    model.itemsModel.appendItems($filemanagerItem.itemsModel.getSelected());
                };

                this.showContinue = ko.pureComputed(function () {
                    return model.itemsModel.objects().length !== 0 && !model.loadingView();
                }, this);


                this.processImport = function () {

                    model.loadingView(true);
                    return $.ajax({
                        type: 'POST',
                        url: apiConnector,
                        timeout: 100000,
                        contentType: "application/json",
                        dataType: 'json',
                        data: JSON.stringify(trimObjectsData(model.itemsModel.objects())),
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader('X-CSRF-Token', token);
                        }
                    }).done(function (response) {
                        if (response.data){
                            if (response.data.length === 0){
                                fi.warning(model.lg.no_supported_types_found);
                                model.loadingView(false);
                            }
                            else {
                                fiModel.modalModel.appendButtons(response.data);
                                fiModel.modalModel.showModal();
                                model.loadingView(false);
                            }
                        }
                        else{
                            fi.warning(model.lg.no_supported_types_found);
                            model.loadingView(false);
                        }
                    }).fail(function (xhr) {
                        fi.error(model.lg.request_failed);
                        model.loadingView(false);
                        handleAjaxError(xhr);
                    }).then(function (response) {
                        if (response.errors) {
                            return $.Deferred().reject();
                        };
                    });
                }
            };

            var ImportItemsModel = function () {
                var items_model = this;
                this.objects = ko.observableArray([]);
                this.listSortField = ko.observable(configSortField);
                this.listSortOrder = ko.observable(configSortOrder);

                this.sortObjects = function () {
                    var sortedList = sortItems(items_model.objects());
                    items_model.objects(sortedList);
                };

                this.appendItems = function (items) {
                    if (!$.isArray(items)) {
                        items = [items];
                    }
                    items.forEach(function (item) {
                        if (item.rdo != null){
                            items_model.appendItem(item);
                        }
                    });
                };

                this.appendItem = function (item) {
                    var newItem = new ImportItemObject(item.rdo),
                        canBeAdded = true,
                        childrenIndexesToRemove = [];
                    ko.utils.arrayForEach(model.itemsModel.objects(), function (objectItem, index) {
                        if (objectItem.rdo === newItem.rdo) {
                            canBeAdded = false;
                            fi.warning(model.lg.item_already_included);
                        }
                        else if (newItem.rdo.attributes.path.startsWith(objectItem.rdo.attributes.path)) {
                            fi.warning(model.lg.item_parent_already_included + ': ' + objectItem.rdo.attributes.name);
                            canBeAdded = false;
                        }
                        else if (objectItem.rdo.attributes.path.startsWith(newItem.rdo.attributes.path)) {
                            childrenIndexesToRemove.push(index);
                        }
                    });
                    if (canBeAdded && childrenIndexesToRemove.length !== 0) {
                        //remove from pickedPathsList on specific index
                        childrenIndexesToRemove.forEach(function (item_index, i) {
                            items_model.objects.splice(item_index - i, 1);
                        });
                        fi.warning(model.lg.item_children_removed);
                    }
                    if (canBeAdded) {
                        items_model.objects.push(newItem);
                        fi.success(model.lg.item_added);
                    }
                };

                this.appendInitialItems = function (items) {
                    if (!$.isArray(items)) {
                        items = [items];
                    }
                    items.forEach(function (item) {
                        if (item.fileData != null){
                            items_model.appendInitialItem(item);
                        }
                    });
                };

                this.appendInitialItem = function (item) {
                    var newItem = new ImportItemObject(item.fileData);
                    items_model.objects.push(newItem);
                };

                this.appendInitialItems(initialData);
                initialData = [];
            };


            var ImportItemObject = function (resourceObject) {
                var item_object = this;

                this.id = resourceObject.id; // for search purpose
                this.itemText = ko.observable(resourceObject.attributes.name);
                this.rdo = resourceObject; // original resource host object
                this.cdo = { // computed host object
                    isFolder: (resourceObject.type === 'folder'),
                    watchFolder: ko.observable(false),
                    type: (resourceObject.type === 'folder' ? 'folder' : 'file')
                };
                this.visible = ko.observable(true);
                this.selected = ko.observable(false);
                this.open = function (item, e) {
                    $filemanagerItem = ko.dataFor(document.getElementById($fileManagerId));
                    if (isFile(this.rdo.attributes.path)) {
                        $filemanagerItem.itemsModel.loadDataList(getClosestNode(this.rdo.attributes.path));
                    }
                    else {
                        $filemanagerItem.itemsModel.loadDataList(this.rdo.attributes.path);
                    }
                };

                this.itemClass = ko.pureComputed(function () {
                    var cssClass = [];
                    return cssClass.join(' ');
                }, this);

                this.title = ko.pureComputed(function () {
                    return this.rdo.id;
                }, this);

                this.watch = function () {
                    if (item_object.cdo.isFolder) {
                        item_object.cdo.watchFolder(!item_object.cdo.watchFolder());
                    }
                };

                this.listIconClass = ko.pureComputed(function () {
                    var cssClass,
                        extraClass = ['ico'];
                    if (this.cdo.isFolder === true) {
                        cssClass = 'ico_folder';
                        extraClass.push('folder');
                    } else {
                        cssClass = 'ico_file';
                        extraClass.push('file');
                    }
                    return cssClass + ' ' + extraClass.join('_');
                }, this);

                this.remove = function () {
                    model.itemsModel.objects.remove(this);
                };
            };


            var TableModel = function () {
                var SortableHeader = function (name) {
                    var thead = this;
                    this.column = ko.observable(name);
                    this.order = ko.observable(model.itemsModel.listSortOrder());

                    this.sortClass = ko.pureComputed(function () {
                        var cssClass;
                        if (model.itemsModel.listSortField() === thead.column()) {
                            cssClass = 'sorted sorted-' + this.order();
                        }
                        return cssClass;
                    }, this);

                    this.sort = function () {
                        var isAscending = thead.order() === 'asc';
                        var isSameColumn = model.itemsModel.listSortField() === thead.column();
                        thead.order(isSameColumn ? (isAscending ? 'desc' : 'asc') : model.itemsModel.listSortOrder());
                        model.itemsModel.listSortField(thead.column());
                        model.itemsModel.listSortOrder(thead.order());
                        model.itemsModel.sortObjects();
                    };
                };

                var DeletableHeader = function (name) {

                    this.column = ko.observable(name);

                    this.delete = function () {
                        model.itemsModel.objects.removeAll();
                    };
                };

                this.thName = new SortableHeader('name');
                this.thWatch = new SortableHeader('watch');
                this.thType = new SortableHeader('type');
                this.thDelete = new DeletableHeader('delete');
            };


            var ModalModel = function () {
                var modal = this,
                    $modalHolder = $('#modalHolder');

                var FormButton = function (type) {
                    var btn = this;
                    this.type = type;
                };

                this.formButtons = ko.observableArray([]);
                this.showModal = function () {
                    $modalHolder.fadeIn();
                };
                this.appendButtons = function (data) {
                    modal.formButtons.removeAll();
                    data.forEach(function (type) {
                      modal.formButtons.push(new FormButton(type));
                    })
                };
                this.closeModal = function(){
                    $modalHolder.fadeOut();
                };

                this.submitForm = function(formButton){
                    return $.ajax({
                        type: 'POST',
                        url: apiConnector + "/type",
                        timeout: 100000,
                        contentType: "application/json",
                        dataType: 'json',
                        data: formButton.type,
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader('X-CSRF-Token', token);
                        }
                    }).done(function (response) {
                        if (response.redirectURL){
                            window.location.replace(response.redirectURL);
                        }
                    }).fail(function (xhr) {
                        fi.error(model.lg.request_failed);
                        handleAjaxError(xhr);
                    }).then(function (response) {
                        if (response.errors) {
                            return $.Deferred().reject();
                        }
                    });
                };

                $modalHolder.hide();
            };

            this.itemsModel = new ImportItemsModel();
            this.tableModel = new TableModel();
            this.controlPanel = new ControlPanelModel();
            this.modalModel = new ModalModel();

        };


        /*---------------------------------------------------------
	 Helper functions
	 ---------------------------------------------------------*/

        var trimObjectsData = function (objects) {
            var result = [];
            objects.forEach(function (item) {
                result.push({
                    'watch': item.cdo.watchFolder(),
                    'fileData' : item.rdo
                })
            });

            return result;
        };

        var isFile = function (path) {
            return path.charAt(path.length - 1) !== '/';
        };

        var buildAjaxRequest = function (method, parameters, dataType) {
            dataType = (typeof dataType === 'undefined') ? 'json' : dataType;

            return $.ajax({
                type: method,
                cache: false,
                url: apiConnector,
                dataType: dataType
            });
        };

        // return closest node for path
        // "/dir/to/"          -->  "/dir/"
        // "/dir/to/file.txt"  -->  "/dir/to/"
        var getClosestNode = function (path) {
            return path.substring(0, path.slice(0, -1).lastIndexOf('/')) + '/';
        };

        // Format server-side response single error object
        var formatServerError = function(errorObject) {
            var message;
            // look for message in case an error CODE is provided
            if (langModel.getLang() && lg(errorObject.title)) {
                message = lg(errorObject.title);
                $.each(errorObject.meta.arguments, function(i, argument) {
                    message = message.replace('%s', argument);
                });
            } else {
                message = errorObject.title;
            }
            return message;
        };


        // Test if a given url exists
        var file_exists = function (url) {
            return $.ajax({
                type: 'HEAD',
                url: url
            });
        };

        // Retrieves config settings from config files
        var loadConfigFile = function (type) {
            var url = null;
            type = (typeof type === 'undefined') ? 'user' : type;

            if (type === 'user') {
                if (_url_.param('config')) {
                    url = fi.settings.baseUrl + '/config/' + _url_.param('config');
                } else {
                    url = fi.settings.configUrl ? fi.settings.configUrl : fi.settings.baseUrl + '/config/fileimport.config.json'; // if configUrl is defined
                }
            } else {
                url = fi.settings.baseUrl + '/config/fileimport.config.default.json';
            }

            return $.ajax({
                type: 'GET',
                url: url,
                dataType: 'json',
                cache: false,
                error: function (response) {
                    fi.error('Given config file (' + url + ') does not exist!');
                }
            });
        };

        // Loads a given js/css files dynamically into header
        var loadAssets = function (assets) {
            for (var i = 0, l = assets.length; i < l; i++) {
                if (typeof assets[i] === 'string') {
                    assets[i] = fi.settings.baseUrl + assets[i];
                }
            }
            toast.apply(this, assets);
        };


        var isKnockoutBounded = function (element) {
            return !!ko.dataFor(element);
        };

        var sortItems = function (items) {
            var sortOrder = fiModel.itemsModel.listSortOrder();
            var sortParams = {
                natural: true,
                order: sortOrder === 'asc' ? 1 : -1,
                cases: false
            };

            items.sort(function (a, b) {
                var sortReturnNumber,
                    aa = getSortSubject(a),
                    bb = getSortSubject(b);

                if (aa === bb) {
                    sortReturnNumber = 0;
                } else {
                    if (aa === undefined || bb === undefined) {
                        sortReturnNumber = 0;
                    } else {
                        if (!sortParams.natural || (!isNaN(aa) && !isNaN(bb))) {
                            sortReturnNumber = aa < bb ? -1 : (aa > bb ? 1 : 0);
                        } else {
                            sortReturnNumber = naturalCompare(aa, bb);
                        }
                    }
                }
                // lastly assign asc/desc
                sortReturnNumber *= sortParams.order;
                return sortReturnNumber;
            });

            /**
             * Get the string/number to be sorted by checking the array value with the criterium.
             * @item KO or treeNode object
             */
            function getSortSubject(item) {
                var sortBy,
                    sortField = fiModel.itemsModel.listSortField();
                switch (sortField) {
                    default:
                        sortBy = item.rdo.attributes.name;
                }

                // strings should be ordered in lowercase (unless specified)
                if (typeof sortBy === 'string') {
                    if (!sortParams.cases) {
                        sortBy = sortBy.toLowerCase();
                    }
                    // spaces/newlines
                    sortBy = sortBy.replace(/\s+/g, ' ');
                }
                return sortBy;
            }

            /**
             * Compare strings using natural sort order
             * http://web.archive.org/web/20130826203933/http://my.opera.com/GreyWyvern/blog/show.dml/1671288
             */
            function naturalCompare(a, b) {
                var aa = chunkify(a.toString()),
                    bb = chunkify(b.toString());
                for (var x = 0; aa[x] && bb[x]; x++) {
                    if (aa[x] !== bb[x]) {
                        var c = Number(aa[x]),
                            d = Number(bb[x]);
                        if (c == aa[x] && d == bb[x]) {
                            return c - d;
                        } else {
                            return aa[x] > bb[x] ? 1 : -1;
                        }
                    }
                }
                return aa.length - bb.length;
            }

            /**
             * Split a string into an array by type: numeral or string
             */
            function chunkify(t) {
                var tz = [], x = 0, y = -1, n = 0, i, j;
                while (i = (j = t.charAt(x++)).charCodeAt(0)) {
                    var m = (i == 46 || (i >= 48 && i <= 57));
                    if (m !== n) {
                        tz[++y] = '';
                        n = m;
                    }
                    tz[y] += j;
                }
                return tz;
            }

            // handle folders position
            var folderItems = [];
            var i = items.length;
            while (i--) {
                if (items[i].rdo.type === 'folder') {
                    folderItems.push(items[i]);
                    items.splice(i, 1);
                }
            }
            if (config.options.folderPosition !== 'top') {
                folderItems.reverse();
            }
            for (var k = 0, fl = folderItems.length; k < fl; k++) {
                if (config.options.folderPosition === 'top') {
                    items.unshift(folderItems[k]);
                } else {
                    items.push(folderItems[k]);
                }
            }

            return items;
        };


        // call the "constructor" method
        construct();
        // fi.setDimensions();
        // $(window).resize(fi.setDimensions);
    };
})(jQuery);

// add the plugin to the jQuery.fn object
$.fn.fileImport = function (options) {

    // iterate through the DOM elements we are attaching the plugin to
    return this.each(function () {

        // if plugin has not already been attached to the element
        if (undefined === $(this).data('fileImportPlugin')) {

            var plugin = new $.fileImportPlugin(this, options);

            $(this).data('fileImportPlugin', plugin);
        }
    });
};

// add location.origin for IE
if (!window.location.origin) {
    window.location.origin = window.location.protocol + '//'
        + window.location.hostname
        + (window.location.port ? ':' + window.location.port : '');
}
