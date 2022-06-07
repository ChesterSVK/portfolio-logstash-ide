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

    $.fileConvertPlugin = function (element, pluginOptions) {
        /**
         * Plugin's default options
         */
        var defaults = {
            baseUrl: '/plug-in/fileconvert',// relative path to the FM plugin folder
            libsUrl: '/libs',
            initialData: [],
            configUrl: null,
            config: {} // configuration options
        };

        /**
         * The reference the current instance of the object
         */
        var fc = this;

        /**
         * Private properties accessible only from inside the plugin
         */
        var $container = $(element),	// reference to the jQuery version of DOM element the plugin is attached to

            config = null,				// configuration options
            apiConnector = null,		// API connector URL to perform requests to server
            configSortField = 'name',		// items sort field name
            configSortOrder = 'asc',		// items sort order 'asc'/'desc'
            fcModel = null,				// fclemanager knockoutJS model
            langModel = null,			// language model
            globalize = null,			// formatting and parsing tool (numbers, dates, etc.)
            delayStack = null,
            initialData = [],
            viewItem = $(element).find('#selectable'),
            token = $("meta[name='_csrf']").attr("content"),

            /** service variables **/
            _url_ = purl(),
            timeStart = new Date().getTime();

        /**
         * This holds the merged default and user-provided options.
         * Plugin's properties will be available through this object like:
         * - fc.propertyName from inside the plugin
         * - element.host('richFilemanager').propertyName from outside the plugin, where "element" is the element the plugin is attached to;
         * @type {{}}
         */

        // The plugin's final settings, contains the merged default and user-provided options (if any)
        fc.settings = $.extend(true, defaults, pluginOptions);


        /*--------------------------------------------------------------------------------------------------------------
	 Public methods
	 Can be called like:
	 - fc.methodName(arg1, arg2, ... argn) from inside the plugin
	 - element.host('richFilemanager').publicMethod(arg1, arg2, ... argn) from outside the plugin,
	   where "element" is the element the plugin is attached to
	--------------------------------------------------------------------------------------------------------------*/

        fc.write = function (message, obj) {
            var log = alertify;
            var options = $.extend({}, {
                reset: true,
                delay: 5000,
                logMaxItems: 5,
                logPosition: 'bottom right',
                logContainerClass: 'fc-log',
                logMessageTemplate: null,
                parent: document.body,
                onClick: undefined,
                unique: false,
                type: 'log'
            }, obj);

            // display only one log for the specified 'logClass'
            if (options.logClass && options.unique && $('.fc-log').children('.' + options.logClass).length > 0) {
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

        fc.error = function (message, options) {
            return fc.write(message, $.extend({}, {
                type: 'error',
                delay: 10000
            }, options));
        };

        fc.warning = function (message, options) {
            return fc.write(message, $.extend({}, {
                type: 'warning',
                delay: 10000
            }, options));
        };

        fc.success = function (message, options) {
            return fc.write(message, $.extend({}, {
                type: 'success',
                delay: 6000
            }, options));
        };

        fc.alert = function (message) {
            alertify
                .reset()
                .dialogContainerClass('fc-popup')
                .alert(message);
        };

        fc.confirm = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fc-popup')
                .confirm(obj.message, obj.okBtn, obj.cancelBtn);
        };

        fc.prompt = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fc-popup')
                .theme(obj.template)
                .prompt(obj.message, obj.value || '', obj.okBtn, obj.cancelBtn);
        };

        fc.dialog = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fc-popup')
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
            return buildAjaxRequest('GET', {}, apiConnector)
                .done(function (response) {
                    if (response.data) {
                        initialData = response.data;
                    }
                }).fail(function (xhr) {
                    fc.error('Unable to perform initial request to server.');
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
                fc.error(errorMessage);
            }
        };

        // Handle JSON server errors.
        var handleJsonErrors = function (errors) {
            fc.console('ERROR JSON', errors);

            $.each(errors, function (i, errorObject) {
                fc.error(formatServerError(errorObject));

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
                    if ($('meta[name=langCode]') !== undefined) {
                        var metaLangCode = $('meta[name=langCode]').attr('content');
                        if (config.language.available.indexOf(metaLangCode) !== -1) {
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
                                    fc.error('Given language file (' + langModel.buildLangFileUrl(urlLangCode) + ') does not exist!');
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
                        baseUrl = fc.settings.baseUrl,
                        libsUrl = fc.settings.libsUrl;

                    return $.when(
                        $.get(libsUrl + '/cldrjs/cldr-dates/' + lang + '/ca-gregorian.json'),
                        $.get(libsUrl + '/cldrjs/cldr-numbers/' + lang + '/numbers.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/likelySubtags.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/timeData.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/weekData.json')
                    ).fail(function () {
                        fc.error('CLDR files for "' + lang + '" language do not exist!');
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
            fcModel = new ConvertModel();
            if (!isKnockoutBounded(element)) {
                ko.applyBindings(fcModel, element);
            }
            // remove loading screen div
            fcModel.loadingView(false);
        };

        /**
         * Language model
         *
         * @constructor
         */
        var LangModel = function () {
            var currentLang = "cs",
                translationsHash = {},
                translationsPath = fc.settings.baseUrl + '/languages/';

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
        var ConvertModel = function () {
            var model = this;
            this.config = ko.observable(config);
            this.loadingView = ko.observable(true);
            this.currentLang = langModel.getLang();
            this.lg = langModel.getTranslations();

            var ConvertItemsModel = function () {
                var items_model = this;
                this.objects = ko.observableArray([]);
                this.originalObjects = ko.observableArray([]);
                this.listSortField = ko.observable(configSortField);
                this.listSortOrder = ko.observable(configSortOrder);
                this.gridOptions = {
                    displaySelectionCheckbox: false,
                    data: this.objects,
                    showGroupPanel: false,
                    jqueryUIDraggable: false,
                    columnDefs: [
                        {
                            field: 'path',
                            displayName: model.lg.path,
                            resizable: false,
                            headerClass: 'text-center',
                            cellClass : 'p-1',
                        },
                        {
                            field: 'remove',
                            displayName: model.lg.delete,
                            resizable: false,
                            sortable: false,
                            width: 100,
                            headerClass: 'text-center',
                            cellClass : 'text-center',
                            cellTemplate: '<button class="btn btn-sm mt-1 btn-info" ' +
                            ' data-bind="click: function(data) { $root.entity.remove() } , attr: {title: \'' + model.lg.delete + '\' }">\n' +
                            ' <i class="fas fa-window-close"></i>\n' +
                            ' </button>'
                        }
                    ],
                    selectWithCheckboxOnly: true,
                    footerVisible: false,
                    headerRowHeight: 40,
                    rowHeight: 40,
                    showColumnMenu: false,
                    showFilter: false
                };

                this.appendItems = function (items) {
                    if (!$.isArray(items)) {
                        items = [items];
                    }
                    items.forEach(function (item) {
                        items_model.appendItem(item);
                    });
                };

                this.appendItem = function (item) {
                    var newItem = new ConvertItemObject(item);
                    items_model.objects.push(newItem);
                };

                this.appendOriginalItems = function (items) {
                    if (!$.isArray(items)) {
                        items = [items];
                    }
                    items.forEach(function (item) {
                        items_model.appendItem(item);
                    });
                };

                this.appendOriginalItem = function (item) {
                    var newItem = new ConvertItemObject(item);
                    items_model.originalObjects.push(newItem);
                };

                this.initialize = function () {
                    if ($.isArray(initialData) && initialData.length) {
                        this.appendItems(initialData);
                        this.appendOriginalItem(initialData);
                    }
                };

                this.initialize();
            };


            var ConvertItemObject = function (resourceObject) {
                var item_object = this;
                this.path = resourceObject;

                this.itemClass = ko.pureComputed(function () {
                    var cssClass = [];
                    return cssClass.join(' ');
                }, this);

                this.remove = function () {
                    console.log('here');
                    model.itemsModel.objects.remove(this);
                };
            };


            var TableModel = function () {
                var table = this;

                this.reloadOriginalPaths = function () {
                    model.itemsModel.objects.removeAll();
                    model.itemsModel.appendItems(initialData);
                };

                var DeletableHeader = function (name) {

                    this.column = ko.observable(name);

                    this.delete = function () {
                        model.itemsModel.objects.removeAll();
                    };
                };

                this.thDelete = new DeletableHeader('delete');
            };

            var HistoryTableModel = function () {
                var history_table = this;
                this.historyItems = ko.observableArray([]);

                this.initTableItems = function (array) {
                    if ($.isArray(array)) {
                        history_table.historyItems.removeAll();
                        history_table.historyItems(array)
                    }
                };
                this.getHistoryItems = function () {
                    return buildAjaxRequest('GET', {}, apiConnector + "/getSavedApplicationData")
                        .done(function (response) {
                            if (response) {
                                history_table.initTableItems(response.data);
                            }
                        }).fail(function (xhr) {
                            fc.error(model.lg.error_gettting_history_items);
                            handleAjaxError(xhr);
                        }).then(function (response) {
                            if (response.errors) {
                                return $.Deferred().reject();
                            }
                        });
                };
                this.useConfiguration = function (item) {
                    model.configCreateModel.convert();
                    return buildAjaxRequest('POST', {
                        logstashCommandId: item.logstashCommandId
                    }, apiConnector + "/useSavedApplicationData")
                        .done(function (response) {
                            if (response) {
                                window.location.href = response.redirectURL;
                            }
                        }).fail(function (xhr) {
                            fc.error(model.lg.error_using_configuration);
                            handleAjaxError(xhr);
                        }).then(function (response) {
                            if (response.errors) {
                                return $.Deferred().reject();
                            }
                        });
                };

                this.getHistoryItems();
            };


            var ConfigCreateModel = function () {
                var create_model = this;

                this.convert = function (data) {
                    if (model.itemsModel.objects().length === 0) {
                        fc.error(model.lg.empty_paths);
                        return;
                    }
                    model.loadingView(true);
                    return $.ajax({
                        type: 'POST',
                        url: apiConnector,
                        timeout: 100000,
                        contentType: "application/json",
                        data: JSON.stringify(getObjectsPaths(model.itemsModel.objects())),
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader('X-CSRF-Token', token);
                        }
                    }).done(function (response) {
                        if (response.redirectURL) {
                            window.location.href = response.redirectURL;
                        }
                    }).fail(function (xhr) {
                        model.loadingView(false);
                        fc.error(model.lg.request_failed);
                        handleAjaxError(xhr);
                        model.loadingView(false);
                    }).then(function (response) {
                        if (response.errors) {
                            return $.Deferred().reject();
                        }
                    });
                }
            };


            var MolochModel = function () {
                var moloch_model = this;
                this.uploadToMoloch = function (data) {
                    if (model.itemsModel.objects().length === 0) {
                        fc.error(model.lg.empty_paths);
                        return;
                    }
                    //Loading screen
                    model.loadingView(true);
                    return $.ajax({
                        type: 'POST',
                        //Url ktora bude definovana v nejakom controlleri v Jave
                        url: apiConnector,
                        timeout: 100000,
                        contentType: "application/json",
                        //Posli mu subory ktore su zvolene
                        data: JSON.stringify(getObjectsPaths(model.itemsModel.objects())),
                        beforeSend: function (xhr) {
                            //Token needed
                            xhr.setRequestHeader('X-CSRF-Token', token);
                        }
                    }).done(function (response) {
                        //Ak je vsetko ok tak metoda v JAVe vracia string na url, na ktoru dame redirect
                        if (response.redirectURL) {
                            window.location.href = response.redirectURL;
                        }
                    }).fail(function (xhr) {
                        //Ak je fail tak vypneme loading a vypiseme chybu
                        model.loadingView(false);
                        fc.error(model.lg.request_failed);
                        handleAjaxError(xhr);
                        model.loadingView(false);
                    }).then(function (response) {
                        if (response.errors) {
                            return $.Deferred().reject();
                        }
                    });
                }
            };


            this.itemsModel = new ConvertItemsModel();
            this.tableModel = new TableModel();
            this.historyTableModel = new HistoryTableModel();
            this.configCreateModel = new ConfigCreateModel();
            this.molochModel = new MolochModel();
        };


        /*---------------------------------------------------------
	 Helper functions
	 ---------------------------------------------------------*/

        var buildAjaxRequest = function (method, parameters, url, dataType) {
            dataType = (typeof dataType === 'undefined') ? 'json' : dataType;
            url = (typeof url === 'undefined') ? apiConnector : url;

            return $.ajax({
                type: method,
                cache: false,
                url: url,
                dataType: dataType,
                data: parameters,
                beforeSend: function (xhr) {
                    if (method === "POST") {
                        xhr.setRequestHeader('X-CSRF-Token', token);
                    }
                }
            });
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
                    url = fc.settings.baseUrl + '/config/' + _url_.param('config');
                } else {
                    url = fc.settings.configUrl ? fc.settings.configUrl : fc.settings.baseUrl + '/config/fileconvert.config.json'; // if configUrl is defined
                }
            } else {
                url = fc.settings.baseUrl + '/config/fileconvert.config.default.json';
            }

            return $.ajax({
                type: 'GET',
                url: url,
                dataType: 'json',
                cache: false,
                error: function (response) {
                    fc.error('Given config file (' + url + ') does not exist!');
                }
            });
        };

        // Loads a given js/css files dynamically into header
        var loadAssets = function (assets) {
            for (var i = 0, l = assets.length; i < l; i++) {
                if (typeof assets[i] === 'string') {
                    assets[i] = fc.settings.baseUrl + assets[i];
                }
            }
            toast.apply(this, assets);
        };


        var isKnockoutBounded = function (element) {
            return !!ko.dataFor(element);
        };

        var getObjectsPaths = function (elements) {
            var paths = [];
            elements.forEach(function (item) {
                paths.push(item.path)
            });
            return paths;
        };


        // Format server-side response single error object
        var formatServerError = function (errorObject) {
            var message;
            // look for message in case an error CODE is provided
            if (langModel.getLang() && lg(errorObject.title)) {
                message = lg(errorObject.title);
                $.each(errorObject.meta.arguments, function (i, argument) {
                    message = message.replace('%s', argument);
                });
            } else {
                message = errorObject.title;
            }
            return message;
        };


        // call the "constructor" method
        construct();
        // fc.setDimensions();
        // $(window).resize(fc.setDimensions);
    };
})(jQuery);

// add the plugin to the jQuery.fn object
$.fn.fileConvert = function (options) {

    // iterate through the DOM elements we are attaching the plugin to
    return this.each(function () {

        // if plugin has not already been attached to the element
        if (undefined === $(this).data('fileConvertPlugin')) {

            var plugin = new $.fileConvertPlugin(this, options);

            $(this).data('fileConvertPlugin', plugin);
        }
    });
};

// add location.origin for IE
if (!window.location.origin) {
    window.location.origin = window.location.protocol + '//'
        + window.location.hostname
        + (window.location.port ? ':' + window.location.port : '');
}
