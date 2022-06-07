(function($) {

    $.neckApplicationPlugin = function(element)
    {
        /**
         * Plugin's default options
         */
        var defaults = {
        };

        /**
         * The reference the current instance of the object
         */
        var neckScript = this;

        /**
         * Private properties accessible only from inside the plugin
         */
        var $container = $(element),	// reference to the jQuery version of DOM element the plugin is attached to
            $navBar             = $('#appNavBar'),
            $footer             = $('#appFooter'),
            $content            = $('#appContent'),
            $flashBar           = $('#appFlashMessages'),
            timeStart           = new Date().getTime();

        // Forces columns to fill the layout vertically.
        // Called on initial page load and on resize.
        neckScript.setDimensions = function() {
            if ($(window).width() >= 768){
                $content.outerHeight( $(window).height() - ($navBar.outerHeight() + $footer.outerHeight()) );
            }

        };

        /*--------------------------------------------------------------------------------------------------------------
	 Private methods
	 These methods can be called only from inside the plugin like: methodName(arg1, arg2, ... argn)
	--------------------------------------------------------------------------------------------------------------*/

        /**
         * The "constructor" method that gets called when the object is created
         */
        var construct = function() {
            var deferred = $.Deferred();

            deferred
                .then(function() {
                    return initialize();
                });

            deferred.resolve();
        };

        var initialize = function () {
            // Activates knockout.js
            // fmModel = new FmModel();
            // ko.applyBindings(fmModel);



            initializeFlashMessages();

            var $loading = $container.find('#app-loading-wrap');
            // remove loading screen div
            $loading.fadeOut(800, function() {
                neckScript.setDimensions();
            });
            neckScript.setDimensions();

            $(window).resize(neckScript.setDimensions());
        };


        /**
         * Initialize Flash Messages
         */
        var initializeFlashMessages = function() {
            // initialise close buttons
            $flashBar.find('.close-alert-btn').click(function (e) {
                e.preventDefault();
                $(this).parent().hide();
            });

            // set timer to fadeout
            $flashBar.find('.alert').delay(5000).fadeOut();
        };


        /**
         * Knockout general model
         *
         * @constructor
         */
        var FmModel = function() {
            var model = this;
            this.config = ko.observable(config);
        };

        // Save CodeMirror editor content to file
        var saveItem = function(resourceObject) {
            buildAjaxRequest('POST', formParams).done(function(response) {
                if(response.data) {
                    // neckScript.success(lg('successful_edit'));
                }
            }).fail(handleAjaxError);
        };

        // Perform "readfile" API request
        var previewItemTest = function(resourceObject) {
            return buildAjaxRequest('GET', {
                mode: 'readfile',
                path: resourceObject.id
            }, 'text').fail(handleAjaxError);
        };

        // call the "constructor" method
        construct();
    };

    $(document).ready(function () {
        $('#neckPluginContainer').each(function() {

            // if plugin has not already been attached to the element
            if (undefined === $(this).data('neckPlugin')) {

                /**
                 * Creates a new instance of the plugin
                 * Pass the DOM element and the user-provided options as arguments
                 */
                var plugin = new $.neckApplicationPlugin(this);

                /**
                 * Store a reference to the plugin object
                 * The plugin are available like:
                 * - element.host('richFilemanager').publicMethod(arg1, arg2, ... argn);  for methods
                 * - element.host('richFilemanager').settings.propertyName;  for properties
                 */
                $(this).data('neckPlugin', plugin);
            }
        });
    });
})(jQuery);