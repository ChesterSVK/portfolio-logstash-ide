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

    $.fileTransformPlugin = function (element, pluginOptions) {
        /**
         * Plugin's default options
         */
        var defaults = {
            baseUrl: '/plug-in/filetransform',// relative path to the FM plugin folder
            libsUrl: '/libs',
            initialData: [],
            configUrl: null,
            config: {} // configuration options
        };

        /**
         * The reference the current instance of the object
         */
        var ft = this;

        /**
         * Private properties accessible only from inside the plugin
         */
        var $container = $(element),	// reference to the jQuery version of DOM element the plugin is attached to

            config = null,				// configuration options
            apiConnector = null,		// API connector URL to perform requests to server
            configSortField = 'name',		// items sort field name
            configSortOrder = 'asc',		// items sort order 'asc'/'desc'
            ftModel = null,				// fclemanager knockoutJS model
            langModel = null,			// language model
            globalize = null,			// formatting and parsing tool (numbers, dates, etc.)
            delayStack = null,
            initialData = [],
            token = $("meta[name='_csrf']").attr("content"),
            viewModes = {default: "default", predefined: "predefined"},
            viewModesFunctions = {simple: "simple", detailed: "detailed"},

            /** service variables **/
            _url_ = purl(),
            timeStart = new Date().getTime();

        /**
         * This holds the merged default and user-provided options.
         * Plugin's properties will be available through this object like:
         * - ft.propertyName from inside the plugin
         * - element.host('richFilemanager').propertyName from outside the plugin, where "element" is the element the plugin is attached to;
         * @type {{}}
         */

        // The plugin's final settings, contains the merged default and user-provided options (if any)
        ft.settings = $.extend(true, defaults, pluginOptions);


        /*--------------------------------------------------------------------------------------------------------------
	 Public methods
	 Can be called like:
	 - ft.methodName(arg1, arg2, ... argn) from inside the plugin
	 - element.host('richFilemanager').publicMethod(arg1, arg2, ... argn) from outside the plugin,
	   where "element" is the element the plugin is attached to
	--------------------------------------------------------------------------------------------------------------*/

        ft.write = function (message, obj) {
            var log = alertify;
            var options = $.extend({}, {
                reset: true,
                delay: 5000,
                logMaxItems: 5,
                logPosition: 'bottom right',
                logContainerClass: 'ft-log',
                logMessageTemplate: null,
                parent: document.body,
                onClick: undefined,
                unique: false,
                type: 'log'
            }, obj);

            // display only one log for the specified 'logClass'
            if (options.logClass && options.unique && $('.ft-log').children('.' + options.logClass).length > 0) {
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

        ft.error = function (message, options) {
            return ft.write(message, $.extend({}, {
                type: 'error',
                delay: 10000
            }, options));
        };

        ft.warning = function (message, options) {
            return ft.write(message, $.extend({}, {
                type: 'warning',
                delay: 10000
            }, options));
        };

        ft.success = function (message, options) {
            return ft.write(message, $.extend({}, {
                type: 'success',
                delay: 6000
            }, options));
        };

        ft.alert = function (message) {
            alertify
                .reset()
                .dialogContainerClass('ft-popup')
                .alert(message);
        };

        ft.confirm = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('ft-popup')
                .confirm(obj.message, obj.okBtn, obj.cancelBtn);
        };

        ft.prompt = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('ft-popup')
                .theme(obj.template)
                .prompt(obj.message, obj.value || '', obj.okBtn, obj.cancelBtn);
        };

        ft.dialog = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('ft-popup')
                .dialog(obj.message, obj.buttons);
        };

        ft.console = function () {
            [].unshift.call(arguments, new Date().getTime());
            console.log.apply(this, arguments);
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
            return buildAjaxRequest('GET', {})
                .done(function (response) {
                    if (response.data) {
                        initialData = response.data;
                    }
                }).fail(function (xhr) {
                    ft.error('Unable to perform initial request to server.');
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
                ft.error(errorMessage);
            }
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
                                    ft.error('Given language file (' + langModel.buildLangFileUrl(urlLangCode) + ') does not exist!');
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
                        baseUrl = ft.settings.baseUrl,
                        libsUrl = ft.settings.libsUrl;

                    return $.when(
                        $.get(libsUrl + '/cldrjs/cldr-dates/' + lang + '/ca-gregorian.json'),
                        $.get(libsUrl + '/cldrjs/cldr-numbers/' + lang + '/numbers.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/likelySubtags.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/timeData.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/weekData.json')
                    ).fail(function () {
                        ft.error('CLDR files for "' + lang + '" language do not exist!');
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
            ftModel = new TransformModel();
            if (!isKnockoutBounded(element)) {
                ko.applyBindings(ftModel, element);
            }
            // remove loading screen div
            ftModel.loadingView(false);
        };

        /**
         * Language model
         *
         * @constructor
         */
        var LangModel = function () {
            var currentLang = "cs",
                translationsHash = {},
                translationsPath = ft.settings.baseUrl + '/languages/';

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
        var TransformModel = function () {
            var model = this;
            this.config = ko.observable(config);
            this.loadingView = ko.observable(true);
            this.currentLang = langModel.getLang();
            this.lg = langModel.getTranslations();

            var AttributeItemsModel = function () {
                var items_model = this;
                this.selectedItems = ko.observableArray([]);
                this.objects = ko.observableArray([]);
                this.gridOptions = {
                    displaySelectionCheckbox: false,
                    selectedItems: this.selectedItems,
                    data: this.objects,
                    showGroupPanel: false,
                    jqueryUIDraggable: false,
                    headerRowHeight: 40,
                    columnDefs: [
                        {
                            field: 'attribute',
                            resizable: false,
                            displayName: '<i class="fas fa-sort-alpha-up"></i>',
                            headerClass: 'text-center',
                            cellClass: 'p-1 text-center',
                            headerCellTemplate: ''
                        }
                    ],
                    selectWithCheckboxOnly: false,
                    footerVisible: false,
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
                    var newItem = new AttributeItemObject(item);
                    items_model.objects.push(newItem);
                };

                this.initialize = function () {
                    if ($.isArray(initialData) && initialData.length) {
                        this.appendItems(initialData);
                    }
                };

                this.initialize();
            };


            var AttributeItemObject = function (resourceObject) {
                var item_object = this;
                this.attribute = resourceObject;
                this.selected = ko.observable(false);

                this.itemClass = ko.pureComputed(function () {
                    var cssClass = [];
                    return cssClass.join(' ');
                }, this);

                this.select = function (item) {
                    item_object.selected(!item_object.selected());
                }
            };

            var FunctionsModel = function () {
                var fModel = this;
                this.viewMode = ko.observable(config.manager.defaultViewMode == null ? viewModes.predefined : config.manager.defaultViewMode);
                this.usedFunctionsObject = ko.observable();

                var FunctionItemObject = function (resourceObject, parent, id) {
                    var item = this;
                    this.parent = parent;
                    this.id = id;
                    this.required = (resourceObject.required !== undefined && resourceObject.required !== null) ? Boolean(resourceObject.required) : false;
                    this.rdo = resourceObject;
                    this.commandName = normalizeCommandName(resourceObject.commandEnum);
                    this.commandArgument = fModel.parseFunctions(resourceObject.commandArgument, item, item.id);
                    this.getClass = function () {
                        var css = [],
                            max = 5,
                            min = 0,
                            number = (max - item.id.split('-').length);
                        css.push('p-' + (number >= min ? number : 0));
                        if (!item.commandArgument.length) {
                            css.push('leaf_command')
                        }
                        else {
                            css.push('node_command')
                        }
                        return css.join(' ');
                    };
                    this.isLeaf = function () {
                        // return (resourceObject.commandInputType !== undefined || resourceObject.commandInputType !== null);
                        return ($('#link' + item.id).hasClass('leaf_command'));
                    };

                    this.postFunction = function () {
                        if (item.isLeaf()) {
                            if ($('#' + item.rdo.commandInputType + 'ModalForm').length) {
                                $('#' + item.rdo.commandInputType + 'ModalForm').modal({
                                    focus: true,
                                    show: true,
                                    keyboard: true
                                });
                                model.formsModel.selectedFunction = item;
                            }
                            else {
                                model.formsModel.selectedFunction = item;
                                model.formsModel.submitCommandWithoutForm();
                            }
                        }
                    }
                };

                var DeleteFunctionItemObject = function (resourceObject, parent, id) {
                    var item = this;
                    this.id = id;
                    this.commandName = normalizeCommandName(resourceObject.commandEnum);
                    this.commandArgument = fModel.parseDeleteFunctions(resourceObject.commandArgument, item, item.id);
                    this.rdo = resourceObject;
                    this.parent = parent;
                    this.deleteFunction = function () {
                        var finalObject = fModel.finalizeArgumentObject(item);
                        return buildAjaxRequest('POST', {
                            mode: "deleteFunction",
                            function: JSON.stringify(finalObject)
                        })
                            .done(function (response) {
                                if (response.data) {
                                    fModel.reloadUsedFunctions(response.data);
                                    ft.success(lg("function_deleted"));
                                }
                            }).fail(function (xhr) {
                                ft.error(lg("request_failed"));
                                handleAjaxError(xhr);
                            }).then(function (response) {
                                if (response.errors) {
                                    return $.Deferred().reject();
                                }
                            });
                    };
                    delete item.rdo.commandArgument;
                };


                this.getUsedFunctions = function () {
                    return buildAjaxRequest('POST', {
                        mode: "getUsedFunctions"
                    })
                        .done(function (response) {
                            if (response.data) {
                                fModel.reloadUsedFunctions(response.data)
                            }
                        }).fail(function (xhr) {
                            ft.error(lg("request_failed"));
                            handleAjaxError(xhr);
                        }).then(function (response) {
                            if (response.errors) {
                                return $.Deferred().reject();
                            }
                        });
                };

                this.reloadUsedFunctions = function (data) {
                    fModel.usedFunctionsObject(data);
                    fModel.usedFunctions.usedFunctionsDetailed(data.configString);
                    fModel.usedFunctions.usedFunctionsSimple.removeAll();
                    fModel.usedFunctions.usedFunctionsSimple(fModel.parseDeleteFunctions(data.commands, null, "Used"));
                };

                this.parseFunctions = function (functionArray, parent, parentIndex) {
                    var res = [];
                    if (!$.isArray(functionArray)) {
                        return [];
                    }
                    functionArray.forEach(function (item, index) {
                        res.push(new FunctionItemObject(item, parent, parentIndex + '-' + index));
                    });
                    return res;
                };

                this.parseDeleteFunctions = function (functionArray, parent, parentIndex) {
                    var res = [];
                    if (!$.isArray(functionArray)) {
                        return [];
                    }
                    functionArray.forEach(function (item, index) {
                        if (canBeAdded(item)) {
                            res.push(new DeleteFunctionItemObject(item, parent, parentIndex + '-' + index));
                        }
                    });
                    return res;
                };


                this.finalizeArgumentObject = function (object) {
                    var lastObject = object,
                        actualObject = object;
                    while (actualObject.parent !== null) {
                        var objectParent = actualObject.parent;
                        objectParent.rdo['commandArgument'] = [actualObject.rdo];
                        actualObject = objectParent;
                        lastObject = actualObject;
                    }
                    return lastObject.rdo;
                };

                this.displayDefault = function () {
                    fModel.viewMode(viewModes.default)
                };


                this.displayPredefined = function () {
                    fModel.viewMode(viewModes.predefined)
                };

                var PredefinedFunctions = function () {
                    var pModel = this;
                    this.rootFunctions = ko.observableArray([]);

                    this.getPredefinedFunctions = function () {
                        return buildAjaxRequest('POST', {
                            mode: "predefinedTransformations"
                        })
                            .done(function (response) {
                                if (response.data) {
                                    pModel.rootFunctions.removeAll();
                                    pModel.rootFunctions(fModel.parseFunctions(response.data, null, "Predefined"));
                                }
                            }).fail(function (xhr) {
                                ft.error(lg("request_failed"));
                                handleAjaxError(xhr);
                            }).then(function (response) {
                                if (response.errors) {
                                    return $.Deferred().reject();
                                }
                            });
                    };

                    this.getPredefinedFunctions();
                };


                var DefaultFunctions = function () {
                    var dModel = this;

                    this.rootFunctions = ko.observableArray([]);

                    this.getDefaultFunctions = function () {
                        return buildAjaxRequest('POST', {
                            mode: "defaultTransformations"
                        })
                            .done(function (response) {
                                if (response.data) {
                                    dModel.rootFunctions.removeAll();
                                    dModel.rootFunctions(fModel.parseFunctions(response.data, null, "Default"));
                                }
                            }).fail(function (xhr) {
                                ft.error(lg("request_failed"));
                                handleAjaxError(xhr);
                            }).then(function (response) {
                                if (response.errors) {
                                    return $.Deferred().reject();
                                }
                            });
                    };

                    this.getDefaultFunctions();
                };

                var UsedFunctions = function () {
                    var used_funcs = this;
                    this.usedFunctionsDetailed = ko.observable("");
                    this.usedFunctionsSimple = ko.observableArray([]);
                    this.viewModeFunctions = ko.observable(config.manager.defaultViewModeFunctions == null ? viewModesFunctions.simple : config.manager.defaultViewModeFunctions);

                    this.reload = function () {
                        fModel.getUsedFunctions();
                    };
                    this.displaySimple = function () {
                        used_funcs.viewModeFunctions(viewModesFunctions.simple);
                    };

                    this.displayDetailed = function () {
                        used_funcs.viewModeFunctions(viewModesFunctions.detailed);
                    };

                    this.resetFunctions = function () {
                        return buildAjaxRequest('POST', {
                            mode: "deleteAllFunctions"
                        })
                            .done(function (response) {
                                if (response.data) {
                                    ft.success(lg('functions_reset'));
                                    fModel.reloadUsedFunctions(response.data);
                                }
                            }).fail(function (xhr) {
                                ft.error(lg("request_failed"));
                                handleAjaxError(xhr);
                            }).then(function (response) {
                                if (response.errors) {
                                    return $.Deferred().reject();
                                }
                            });
                    };
                };

                this.getUsedFunctions();

                this.predefinedFunctions = new PredefinedFunctions();
                this.defaultFunctions = new DefaultFunctions();
                this.usedFunctions = new UsedFunctions();
            };

            var FormsModel = function () {
                var forms_model = this;

                this.selectedFunction = null;
                this.inputItems = ko.observableArray([]);
                this.initInputItems = function (array) {
                    var res = [];
                    array.forEach(function (item) {
                        res.push(new InputItem({value: item.attribute}))
                    });
                    forms_model.inputItems.removeAll();
                    forms_model.inputItems(res);
                };

                function InputItem(data) {
                    this.value = data.value;
                }

                this.getSelectedAttributeItem = function () {
                    return model.itemsModel.selectedItems();
                };

                this.finalizeArgumentObject = function (object, leafArgument) {
                    var lastObject = null,
                        actualObject = object;
                    if (leafArgument !== null) {
                        actualObject.rdo.commandArgument = leafArgument;
                    }
                    while (actualObject.parent !== null) {
                        var objectParent = actualObject.parent;
                        objectParent.rdo['commandArgument'] = [actualObject.rdo];
                        actualObject = objectParent;
                        lastObject = actualObject;
                    }
                    return lastObject.rdo;
                };

                this.submitCommandWithoutForm = function () {
                    var finalObject = forms_model.finalizeArgumentObject(forms_model.selectedFunction, null);
                    return buildAjaxRequest('POST', {
                        mode: "addFunction",
                        function: JSON.stringify(finalObject)
                    })
                        .done(function (response) {
                            if (response.data) {
                                ft.success(lg('function_added'));
                                model.functionsModel.reloadUsedFunctions(response.data);
                            }
                        }).fail(function (xhr) {
                            ft.error(lg("request_failed"));
                            handleAjaxError(xhr);
                        }).then(function (response) {
                            if (response.errors) {
                                return $.Deferred().reject();
                            }
                        });
                };
                forms_model.submitCommand = function (argument, form) {
                    if (forms_model.selectedFunction !== null) {
                        var finalObject = forms_model.finalizeArgumentObject(forms_model.selectedFunction, argument);

                        return buildAjaxRequest('POST', {
                            mode: "addFunction",
                            function: JSON.stringify(finalObject)
                        })
                            .done(function (response) {
                                if (response.data) {
                                    form.formElement.modal('hide');
                                    form.resetForm();
                                    ft.success(lg('function_added'));
                                    model.functionsModel.reloadUsedFunctions(response.data);
                                }
                            }).fail(function (xhr) {
                                ft.error(lg("request_failed"));
                                handleAjaxError(xhr);
                            }).then(function (response) {
                                if (response.errors) {
                                    return $.Deferred().reject();
                                }
                            });
                    }
                };

                var StringForm = function () {
                    var string_form = this;
                    this.formElement = $("#LSTRINGModalForm");
                    this.newItemValue = ko.observable("");
                    this.initFromSelectedItems = function () {
                        var arr = forms_model.getSelectedAttributeItem();
                        if (arr != null && $.isArray(arr)) {
                            if (arr.length) {
                                string_form.newItemValue(arr[0].attribute);
                            }
                            else {
                                ft.warning(lg('empty_items'));
                            }
                        }
                    };
                    string_form.submitCommand = function () {
                        if (string_form.newItemValue() === "") {
                            ft.warning(lg('empty_argument'))
                        }
                        else {
                            forms_model.submitCommand(string_form.getItemAsString(), string_form);
                        }
                    };
                    this.resetForm = function () {
                        string_form.newItemValue("");
                    };
                    this.getItemAsString = function () {
                        return string_form.newItemValue();
                    }
                };

                var BooleanForm = function () {
                    var boolean_form = this;
                    this.formElement = $("#LBOOLModalForm");
                    this.newItemValue = ko.observable(false);
                    boolean_form.submitCommand = function () {
                        forms_model.submitCommand(boolean_form.getItemAsString(), boolean_form);
                    };
                    this.resetForm = function () {
                        boolean_form.newItemValue(false);
                    };
                    this.getItemAsString = function () {
                        return boolean_form.newItemValue();
                    }
                };


                var MethodForm = function () {
                    var method_form = this;
                    this.formElement = $("#LANONYMIZE_ALGOModalForm");
                    this.newItemValue = ko.observable("");
                    this.algos = config.formatter.anonymizeAlgorithms;
                    method_form.submitCommand = function () {
                        if (method_form.newItemValue() === "") {
                            ft.warning(lg('empty_argument'))
                        }
                        else {
                            forms_model.submitCommand(method_form.getItemAsString(), method_form);
                        }
                    };
                    this.resetForm = function () {
                        method_form.newItemValue("");
                    };
                    this.getItemAsString = function () {
                        return method_form.newItemValue();
                    }
                };

                var TimestampArrayForm = function () {
                    var tArray_form = this;
                    this.formElement = $("#LTIMESTAMP_ARRAYModalForm");
                    this.newItemValue = ko.observable("");
                    this.selectedFormat = ko.observable("");
                    this.iItems = ko.observableArray([]);
                    this.availableFormats = config.formatter.dateformats;

                    this.initFromSelectedItems = function () {
                        var arr = forms_model.getSelectedAttributeItem();
                        if (arr != null && $.isArray(arr)) {
                            if (arr.length) {
                                arr.forEach(function (item) {
                                    tArray_form.iItems.push(item);
                                })
                            }
                            else {
                                ft.warning(lg('empty_items'));
                            }
                        }
                    };
                    this.addItem = function () {
                        tArray_form.iItems.push(new AttributeItemObject(tArray_form.selectedFormat()));
                        tArray_form.selectedFormat("");
                    };
                    this.removeItem = function (item) {
                        tArray_form.iItems.remove(item);
                    };
                    tArray_form.submitCommand = function () {
                        if (tArray_form.newItemValue() === "") {
                            ft.warning(lg('empty_argument'))
                        }
                        else {
                            forms_model.submitCommand(tArray_form.createArgument(), tArray_form);
                        }
                    };
                    this.resetForm = function () {
                        tArray_form.newItemValue("");
                        tArray_form.iItems.removeAll();
                    };
                    this.createArgument = function () {
                        var res = [tArray_form.newItemValue()];
                        tArray_form.iItems().forEach(function (item) {
                            res.push(item.attribute);
                        });
                        return res;
                    }
                };

                var ArrayForm = function () {
                    var array_form = this;
                    this.formElement = $("#LARRAYModalForm");
                    this.iItems = ko.observableArray([]);
                    this.newItemText = ko.observable("");
                    this.initFromSelectedItems = function () {
                        var arr = forms_model.getSelectedAttributeItem();
                        if (arr != null && $.isArray(arr)) {
                            if (arr.length) {
                                arr.forEach(function (item) {
                                    array_form.iItems.push(item);
                                })
                            }
                            else {
                                ft.warning(lg('empty_items'));
                            }
                        }
                    };
                    this.addItem = function () {
                        array_form.iItems.push(new AttributeItemObject(array_form.newItemText()));
                        array_form.newItemText("");
                    };
                    this.removeItem = function (item) {
                        array_form.iItems.remove(item);
                    };
                    array_form.submitCommand = function () {
                        if (array_form.iItems().length === 0) {
                            ft.warning(lg('empty_arguments'))
                        }
                        else {
                            forms_model.submitCommand(array_form.getItemsAsArray(), array_form);
                        }
                    };
                    this.resetForm = function () {
                        array_form.iItems.removeAll();
                    };
                    this.getItemsAsArray = function () {
                        var arr = [];
                        array_form.iItems().forEach(function (item) {
                            arr.push(item.attribute);
                        });
                        return arr;
                    }
                };

                var MapForm = function () {
                    var map_form = this;

                    this.formElement = $("#LHASH_MAPModalForm");

                    var MapItem = function (key, value) {
                        this.key = ko.observable(key);
                        this.value = ko.observable(value);
                    };

                    this.iItems = ko.observableArray([]);
                    this.newItemKeyText = ko.observable("");
                    this.newItemValueText = ko.observable("");
                    this.initFromSelectedItems = function () {
                        var arr = forms_model.getSelectedAttributeItem();
                        if (arr != null && $.isArray(arr)) {
                            if (arr.length) {
                                arr.forEach(function (item) {
                                    map_form.iItems.push(new MapItem(item.attribute, ""));
                                })
                            }
                            else {
                                ft.warning(lg('empty_items'));
                            }
                        }
                    };
                    this.addItem = function () {
                        map_form.iItems.push(new MapItem(map_form.newItemKeyText(), map_form.newItemValueText()));
                        map_form.newItemKeyText("");
                        map_form.newItemValueText("");
                    };
                    this.removeItem = function (item) {
                        map_form.iItems.destroy(item);
                    };
                    map_form.submitCommand = function () {
                        if (map_form.iItems().length === 0) {
                            ft.warning(lg('empty_arguments'))
                        }
                        else {
                            forms_model.submitCommand(map_form.getItemsAsMap(), map_form);
                        }
                    };
                    this.resetForm = function () {
                        map_form.iItems.removeAll();
                    };
                    this.getItemsAsMap = function () {
                        var map = {};
                        map_form.iItems().forEach(function (item) {
                            map[item.key()] = item.value();
                        });
                        return map;
                    }
                };

                this.stringForm = new StringForm();
                this.arrayForm = new ArrayForm();
                this.mapForm = new MapForm();
                this.timestampArrayForm = new TimestampArrayForm();
                this.methodForm = new MethodForm();
                this.booleanForm = new BooleanForm();
            };


            this.itemsModel = new AttributeItemsModel();
            this.functionsModel = new FunctionsModel();
            this.formsModel = new FormsModel();
        };


        /*---------------------------------------------------------
	 Helper functions
	 ---------------------------------------------------------*/

        var lg = function (key) {
            return langModel.translate(key);
        };

        var normalizeCommandName = function (string) {
            return string.split("#")[0].toLowerCase().capitalize().replace("_", " ");
        };

        var canBeAdded = function (item) {
            if (item.commandArgument === undefined) {
                return false;
            }
            if (item.commandEnum === undefined) {
                return false;
            }
            return true;
        };

        var buildAjaxRequest = function (method, parameters, dataType) {
            dataType = (typeof dataType === 'undefined') ? 'json' : dataType;

            return $.ajax({
                type: method,
                cache: false,
                url: apiConnector,
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

        var sortItems = function (items) {

            items.sort(function (a, b) {
                return a.attribute > b.attribute ? 1 : -1;
            });

            return items;
        };

        // Handle JSON server errors.
        var handleJsonErrors = function (errors) {
            ft.console('ERROR JSON', errors);

            $.each(errors, function (i, errorObject) {
                ft.error(formatServerError(errorObject));

                if (errorObject.meta.redirect) {
                    window.location.href = errorObject.meta.redirect;
                }
            });
        };

        // Retrieves config settings from config files
        var loadConfigFile = function (type) {
            var url = null;
            type = (typeof type === 'undefined') ? 'user' : type;

            if (type === 'user') {
                if (_url_.param('config')) {
                    url = ft.settings.baseUrl + '/config/' + _url_.param('config');
                }
                else {
                    url = ft.settings.baseUrl + '/config/filetransform.config.json';
                }
            } else {
                url = ft.settings.baseUrl + '/config/filetransform.config.json';
            }

            return $.ajax({
                type: 'GET',
                url: url,
                dataType: 'json',
                cache: false,
                error: function (response) {
                    ft.error('Given config file (' + url + ') does not exist!');
                }
            });
        };

        // Loads a given js/css files dynamically into header
        var loadAssets = function (assets) {
            for (var i = 0, l = assets.length; i < l; i++) {
                if (typeof assets[i] === 'string') {
                    assets[i] = ft.settings.baseUrl + assets[i];
                }
            }
            toast.apply(this, assets);
        };


        var isKnockoutBounded = function (element) {
            return !!ko.dataFor(element);
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
        // ft.setDimensions();
        // $(window).resize(ft.setDimensions);
    };
})(jQuery);

// add the plugin to the jQuery.fn object
$.fn.fileTransform = function (options) {

    // iterate through the DOM elements we are attaching the plugin to
    return this.each(function () {

        // if plugin has not already been attached to the element
        if (undefined === $(this).data('fileTransformPlugin')) {

            var plugin = new $.fileTransformPlugin(this, options);

            $(this).data('fileTransformPlugin', plugin);
        }
    });
};

// add location.origin for IE
if (!window.location.origin) {
    window.location.origin = window.location.protocol + '//'
        + window.location.hostname
        + (window.location.port ? ':' + window.location.port : '');
}

String.prototype.capitalize = function () {
    return this.charAt(0).toUpperCase() + this.slice(1);
};
