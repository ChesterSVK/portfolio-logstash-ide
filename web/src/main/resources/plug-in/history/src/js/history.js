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

    $.historyManager = function (element, pluginOptions) {
        /**
         * Plugin's default options
         */
        var defaults = {
            baseUrl: '/plug-in/history',	// relative path to the FM plugin folder
            libsUrl: '/libs',
            configUrl: null,
            config: {} // configuration options
        };

        /**
         * The reference the current instance of the object
         */
        var fh = this;

        /**
         * Private properties accessible only from inside the plugin
         */
        var $container = $(element),	// reference to the jQuery version of DOM element the plugin is attached to
            // $controlsColumn = $container.find('.controls-column'),

            config = null,				// configuration options
            apiConnector = null,		// API connector URL to perform requests to server
            fhModel = null,				// filemanager knockoutJS model
            langModel = null,			// language model
            globalize = null,			// formatting and parsing tool (numbers, dates, etc.)
            delayStack = null,
            initialData = [],
            token = $("meta[name='_csrf']").attr("content"),


            /** service variables **/
            _url_ = purl(),
            timeStart = new Date().getTime();

        /**
         * This holds the merged default and user-provided options.
         * Plugin's properties will be available through this object like:
         * - fh.propertyName from inside the plugin
         * - element.host('richFilemanager').propertyName from outside the plugin, where "element" is the element the plugin is attached to;
         * @type {{}}
         */

        // The plugin's final settings, contains the merged default and user-provided options (if any)
        fh.settings = $.extend(true, defaults, pluginOptions);


        /*--------------------------------------------------------------------------------------------------------------
	 Public methods
	 Can be called like:
	 - fh.methodName(arg1, arg2, ... argn) from inside the plugin
	 - element.host('richFilemanager').publicMethod(arg1, arg2, ... argn) from outside the plugin,
	   where "element" is the element the plugin is attached to
	--------------------------------------------------------------------------------------------------------------*/

        fh.write = function (message, obj) {
            var log = alertify;
            var options = $.extend({}, {
                reset: true,
                delay: 5000,
                logMaxItems: 5,
                logPosition: 'bottom right',
                logContainerClass: 'fh-log',
                logMessageTemplate: null,
                parent: document.body,
                onClick: undefined,
                unique: false,
                type: 'log'
            }, obj);

            // display only one log for the specified 'logClass'
            if (options.logClass && options.unique && $('.fh-log').children('.' + options.logClass).length > 0) {
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

        fh.error = function (message, options) {
            return fh.write(message, $.extend({}, {
                type: 'error',
                delay: 10000
            }, options));
        };

        fh.warning = function (message, options) {
            return fh.write(message, $.extend({}, {
                type: 'warning',
                delay: 10000
            }, options));
        };

        fh.success = function (message, options) {
            return fh.write(message, $.extend({}, {
                type: 'success',
                delay: 6000
            }, options));
        };

        fh.alert = function (message) {
            alertify
                .reset()
                .dialogContainerClass('fh-popup')
                .alert(message);
        };

        fh.confirm = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fh-popup')
                .confirm(obj.message, obj.okBtn, obj.cancelBtn);
        };

        fh.prompt = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fh-popup')
                .theme(obj.template)
                .prompt(obj.message, obj.value || '', obj.okBtn, obj.cancelBtn);
        };

        fh.dialog = function (obj) {
            alertify
                .reset()
                .dialogWidth(obj.width)
                .dialogPersistent(obj.persistent)
                .dialogContainerClass('fh-popup')
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
            return $.when(loadConfigFile()).done(function (conf) {
                var def_config = conf;

                // remove version from user config file
                if (def_config !== undefined && def_config !== null) {
                    delete def_config.version;
                }

                // setup apiConnector
                if (def_config.api.connectorUrl) {
                    apiConnector = def_config.api.connectorUrl;
                }
                config = def_config;
            });
        };

        // performs initial request to server to retrieve initial params
        var performInitialRequest = function () {
            return buildAjaxRequest('GET', {}).done(function (response) {
                if (response.data) {
                    initialData = response.data;
                }
            }).fail(function (xhr) {
                fh.error('Unable to perform initial request to server.');
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
                // fh.console('ERROR TEXT', errorMessage);
                fh.error(errorMessage);
            }
        };

        // Handle JSON server errors.
        var handleJsonErrors = function (errors) {
            fh.console('ERROR JSON', errors);

            $.each(errors, function (i, errorObject) {
                fh.error(formatServerError(errorObject));

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
                                    fh.error('Given language file (' + langModel.buildLangFileUrl(urlLangCode) + ') does not exist!');
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
                        baseUrl = fh.settings.baseUrl,
                        libsUrl = fh.settings.libsUrl;

                    return $.when(
                        $.get(libsUrl + '/cldrjs/cldr-dates/' + lang + '/ca-gregorian.json'),
                        $.get(libsUrl + '/cldrjs/cldr-numbers/' + lang + '/numbers.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/likelySubtags.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/timeData.json'),
                        $.get(libsUrl + '/cldrjs/cldr-core/supplemental/weekData.json')
                    ).fail(function () {
                        fh.error('CLDR files for "' + lang + '" language do not exist!');
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
            fhModel = new HistoryModel();
            if (!isKnockoutBounded(element)) {
                ko.applyBindings(fhModel, element);
            }
            // remove loading screen div
            fhModel.loadingView(false);
        };

        /**
         * Language model
         *
         * @constructor
         */
        var LangModel = function () {
            var currentLang = "cs",
                translationsHash = {},
                translationsPath = fh.settings.baseUrl + '/languages/';

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
        var HistoryModel = function () {
            var model = this;
            this.config = ko.observable(config);
            this.loadingView = ko.observable(true);
            this.currentLang = langModel.getLang();
            this.lg = langModel.getTranslations();

            var ItemsModel = function () {
                var iModel = this;
                iModel.lg = model.lg;

                iModel.configurations = ko.observableArray([]);

                iModel.initialize = function(){
                    return buildAjaxRequest('GET', {}, '/getAll')
                        .done(function (response) {
                            if (response.data) {
                                iModel.configurations(response.data);
                                if (iModel.configurations().length === 0){
                                    fh.warning(model.lg["no_saved_configs"]);
                                }
                            }
                        }).fail(function (xhr) {
                            fh.error(model.lg("request_failed"));
                            handleAjaxError(xhr);
                        }).then(function (response) {
                            if (response.errors) {
                                return $.Deferred().reject();
                            }
                        });
                };

                // iModel.exportLogstashCommand = function(item){
                //     console.log(item);
                // };

                iModel.deleteConfig = function(item){
                    return buildAjaxRequest('POST',
                        JSON.stringify(modifyBeforeSend(item))
                    , '/deleteItem')
                        .done(function (response) {
                            if (response.data) {
                                iModel.configurations.remove(item);
                                fh.success(model.lg['item_deleted'])
                            }
                        }).fail(function (xhr) {
                            fh.error(model.lg("request_failed"));
                            handleAjaxError(xhr);
                        }).then(function (response) {
                            if (response.errors) {
                                return $.Deferred().reject();
                            }
                        });
                };

                iModel.initialize()
            };

            this.itemsModel = new ItemsModel();
        };


        /*---------------------------------------------------------
	 Helper functions
	 ---------------------------------------------------------*/

        var modifyBeforeSend = function (item) {
            item.type = item.type.toUpperCase();
            return item;
        };
        var buildAjaxRequest = function (method, parameters, urlSuffix) {
            var finalUrl = apiConnector;
            if (urlSuffix !== undefined && urlSuffix !== null){
                finalUrl += urlSuffix;
            }

            return $.ajax({
                type: method,
                cache: false,
                url: finalUrl,
                dataType: 'json',
                contentType : 'application/json; charset=utf-8',
                data: parameters,
                beforeSend: function (xhr) {
                    if (method === 'POST'){
                        xhr.setRequestHeader('X-CSRF-Token', token);
                    }
                }
            });
        };

        // return closest node for path
        // "/dir/to/"          -->  "/dir/"
        // "/dir/to/file.txt"  -->  "/dir/to/"
        var getClosestNode = function (path) {
            return path.substring(0, path.slice(0, -1).lastIndexOf('/')) + '/';
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


        // Test if a given url exists
        var file_exists = function (url) {
            return $.ajax({
                type: 'HEAD',
                url: url
            });
        };

        // Retrieves config settings from config files
        var loadConfigFile = function () {
            var url = null;
            url = fh.settings.baseUrl + '/config/history.config.json';

            return $.ajax({
                type: 'GET',
                url: url,
                dataType: 'json',
                cache: false,
                error: function (response) {
                    fh.error('Given config file (' + url + ') does not exist!');
                }
            });
        };

        // Loads a given js/css files dynamically into header
        var loadAssets = function (assets) {
            for (var i = 0, l = assets.length; i < l; i++) {
                if (typeof assets[i] === 'string') {
                    assets[i] = fh.settings.baseUrl + assets[i];
                }
            }
            toast.apply(this, assets);
        };


        var isKnockoutBounded = function (element) {
            return !!ko.dataFor(element);
        };

        // call the "constructor" method
        construct();
        // fh.setDimensions();
        // $(window).resize(fh.setDimensions);
    };
})(jQuery);

// add the plugin to the jQuery.fn object
$.fn.hManager = function (options) {

    // iterate through the DOM elements we are attaching the plugin to
    return this.each(function () {

        // if plugin has not already been attached to the element
        if (undefined === $(this).data('historyManager')) {

            var plugin = new $.historyManager(this, options);

            $(this).data('historyManager', plugin);
        }
    });
};

// add location.origin for IE
if (!window.location.origin) {
    window.location.origin = window.location.protocol + '//'
        + window.location.hostname
        + (window.location.port ? ':' + window.location.port : '');
}
