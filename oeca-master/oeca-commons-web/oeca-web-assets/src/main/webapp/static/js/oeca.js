$(function () {
    //disable links which are marked with disabled class
    $('body').on('click', 'a.disabled', function (event) {
        oeca.notifications.showWarnMessage("We are sorry but this feature is currently under construction.");
        event.preventDefault();
    });
    //hide popovers on body click
    $('body').on('click', function (e) {
        $('dfn[data-bind^="popover"]').each(function () {
            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide');
            }
        });
    });
    $('li.disabled > a').not('li.paginate_button > a').on('click', null, function (event) {
        oeca.notifications.showWarnMessage("We are sorry but this feature is currently under construction.");
        event.preventDefault();
    });

    $(document).on('shown.bs.modal', '.modal', function () {
        var zIndex = 1040 + (10 * $('.modal:visible').length);
        $(this).css('z-index', zIndex);
        $(this).children('.modal-dialog').css('z-index', zIndex);
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        var elements = $(this).find('input:first, select:first, textarea:first');
        if (elements && elements.length > 0) {
            elements[0].focus();
        }
    });

    //Validation
    ko.validation.rules.pattern.message = 'Invalid.';

    ko.validation.init({
        registerExtenders: true,
        messagesOnModified: true,
        insertMessages: true,
        parseInputAttributes: true,
        messageTemplate: null,
        decorateInputElement: true,
        writeInputAttributes: true,
        errorElementClass: 'error'
    }, true);

    //Modifies validation grouping to focus on first error element
    var validationGrouping = ko.validation.group;
    ko.validation.group = function group(obj, options) {
        var result = validationGrouping(obj, options);
        var showAllMessages = result.showAllMessages;
        result.showAllMessages = function (show) {
            showAllMessages(show);
            var $elem = $(".validationMessage:visible").first();

            if ($elem.length > 0 && $(".modal-dialog").is(":visible") == false) {
                $('html, body').animate({
                    scrollTop: $elem.offset().top - 175
                }, 1000);
            }
        };

        return result;
    };

    ko.validation.insertValidationMessage = function insertValidationMessage(element) {
        var span = document.createElement('SPAN');
        span.className = ko.validation.utils.getConfigOptions(element).errorMessageClass;
        var typeAttr = element.getAttribute('type');
        var nameAttr = element.getAttribute('name');
        if (typeof typeAttr === "undefined" || !typeAttr)
        {
            typeAttr = "text";
        }
        if ($.inArray(typeAttr,["radio", "checkbox"]) < 0) {
            if(element.parentNode.className == "input-group") {
                var parent = element.parentNode;
                parent.parentNode.appendChild(document.createElement("BR"));
                parent.parentNode.appendChild(span);
            } else {
                ko.validation.utils.insertAfter(element, span);
            }
        }
        else if ($('input[name*="'+nameAttr+'"]').length > 1) {
            if ($('span[name*="'+nameAttr+'"]').length == 0) {
                span.setAttribute("name", nameAttr);
                var parentDiv = element.parentNode;
                while(parentDiv.tagName !== "DIV") {
                    parentDiv = parentDiv.parentNode;
                }
                parentDiv.parentNode.insertBefore(span,parentDiv.previousSibling);
            }
        } else {
            element.parentNode.insertBefore(span, element);
            element.parentNode.insertBefore(document.createElement("BR"), element);
        }
        return span;
    };

    //Allow date fields to be validatable
    ko.validation.makeBindingHandlerValidatable("date");
    ko.validation.makeBindingHandlerValidatable("bootstrapSwitchOn");
    ko.validation.makeBindingHandlerValidatable("text");
    ko.validation.makeBindingHandlerValidatable("handlerId");


    //Temporarily save KO MultiSelect binding handler in order to override
    var originalMultiselectInit = ko.bindingHandlers.multiselect.init;

    //Override KO MultiSelect init binding handler
    ko.bindingHandlers.multiselect.init = function (element, valueAccessor, allBindings, viewModel, bindingContext) {
        originalMultiselectInit(element, valueAccessor, allBindings, viewModel, bindingContext);

        var $elem = $(element).next('div').find('button.multiselect.dropdown-toggle');

        $elem.click(function () {
            //Focus on the search filter input element
            var $searchInput = $(element).next('div').find('ul.multiselect-container').find('.multiselect-search');
            var $clearButton = $(element).next('div').find('ul.multiselect-container').find('.multiselect-clear-filter');

            var $listItems = $(element).next('div').find('ul.multiselect-container').find('li').not(".multiselect-item.multiselect-filter");

            $searchInput.on('keydown', function (e) {
                var keyCode = e.keyCode || e.which;
                if (keyCode == 13) {
                    e.preventDefault();

                    if ($listItems.length > 0) {
                        setTimeout(function () {
                            $($listItems).not('.multiselect-filter-hidden').first().find('a').focus();
                        }, 100);
                    }
                }
            });

            $listItems.on('keydown', function (e) {
                var keyCode = e.keyCode || e.which;
                if (keyCode == 27) {
                    e.preventDefault();
                    $searchInput.focus();
                }
            });

            setTimeout(function () {
                $searchInput.focus();
            }, 100);
        });

        $(element).attr('tabindex', '-1');
    };


    //Temporarily save KO Validation binding handler in order to override
    var originalValidationElementUpdate = ko.bindingHandlers.validationElement.update;

    //Override KO Validation Element binding handler
    ko.bindingHandlers.validationElement.update = function (element, valueAccessor, allBindingsAccessor) {
        $.each(allBindingsAccessor().validationElement.rules(), function (index, validator) {
            if (validator.rule == 'required') {
                if (typeof validator.condition != 'undefined') {
                    if (validator.condition() === true) {
                        //Set indicator because required condition resolves as true
                        oeca.validation.setRequiredElementIndicator(element);
                    } else {
                        //Remove indicator because required condition resolves as false
                        oeca.validation.removeRequiredElementIndicator(element);
                    }
                } else {
                    //Set indicator because this field is always required
                    oeca.validation.setRequiredElementIndicator(element);
                }
            }
        });
        //Call original binding handler to invoke native KO Validation functionality
        originalValidationElementUpdate(element, valueAccessor, allBindingsAccessor);
    };

    //Setting error CSS on Select2 Dropdown (since it's not supported by KO Validation by default)
    var originalBindingCssUpdate = ko.bindingHandlers.css.update;
    ko.bindingHandlers.css.update = function (element, cssSettingsAccessor, allBindingsAccessor) {
        var isValid = !cssSettingsAccessor().error;

        if ($(element).hasClass("select2-offscreen")) {
            var $select2Container = $(element).prev('.select2-container');
            if (isValid) {
                $select2Container.removeClass('error');
            } else {
                $select2Container.addClass('error');
            }
        }

        if ($(element).is(':hidden')) {
            var $multiselectContainer = $(element).next('div.btn-group').find('button.multiselect');
            if (isValid) {
                $multiselectContainer.removeClass('error');
            } else {
                $multiselectContainer.addClass('error');
            }
        }

        originalBindingCssUpdate(element, cssSettingsAccessor, allBindingsAccessor);
    };

    ko.bindingHandlers.fadeVisible = {
        init: function(element, valueAccessor) {
            // Initially set the element to be instantly visible/hidden depending on the value
            var value = valueAccessor();
            $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
        },
        update: function(element, valueAccessor) {
            // Whenever the value subsequently changes, slowly fade the element in or out
            var value = valueAccessor();
            ko.unwrap(value) ? $(element).fadeIn() : $(element).fadeOut();
        }
    };


    //Pager
    pager.Href.hash = '#!/';


    //Ajax Handlers
    $(document).ajaxComplete(function (event, req, settings) {
        if (req !== undefined && req.status !== 200 && req.status !== 0 && req.status !== 204) {
            if (req.status == 401) {
                oeca.notifications.showErrorMessage("You have been logged out.  " +
                    "You will be redirected to the login page momentarily.", null,
                    function () {
                        window.location.href = req.getResponseHeader("redirect-url");
                    });
                return;
            }

            if (req.status == 403) {
                oeca.notifications.showErrorMessage("You are not authorized to access this action.  " +
                    "You will be redirected to the login page momentarily.", null,
                    function () {
                        window.location.href = config.ctx + "/action/login";
                    });
                return;
            }

            if (req.status == 500) {
                var defaultMessage = "We are very sorry, but it looks like we encountered a problem.";
                try {
                    var response = JSON.parse(req.responseText);
                    if (response.code !== "E_ValidationError" && response.code !== "E_WeakPassword") {
                        oeca.notifications.showErrorMessage(defaultMessage + " " + response.message, {title: "Error"});
                    }
                }
                catch (err) {
                    oeca.logger
                        .debug("Something went wrong parsing the server error response")
                        .debug(err);

                    oeca.notifications.showErrorMessage(defaultMessage);
                }

                return;
            }

            oeca.notifications.showErrorMessage("We are very sorry, but it looks like we encountered a problem. \n\n" + req.responseText);
        }
    });


    $(document).ajaxStart(function () {
        oeca.blockUI.showWithDelay()
    });

    $(document).ajaxStop(function () {
        oeca.blockUI.hide();
    });
});

Array.prototype.contains = function(element){
    return this.indexOf(element) > -1;
};

//OECA Namespace
var oeca = {
    accountStatuses: {
        ACTIVE: "Active",
        UNVERIFIED: "Unverified",
        NEWLYREGISTERED: "NewlyRegistered",
        PENDINGUPDATE: "PendingUpdate",
        INACTIVE: "Inactive"
    },
    requestStatuses: {
        PENDING: "Pending",
        GRANTED: "Granted",
        DENIED: "Denied"
    },
    logger: function () {
        var result = {
            enabled: true,
            debug: function (msg) {
                if (result.enabled) {
                    //Needed for IE9 and previous browsers that don't have console defined
                    if (!window.console) console = {
                        log: function () {
                        }
                    };
                } else {
                    return result;
                }

                if (console !== undefined) {
                    console.log(msg);
                }

                return result;
            }
        }

        return result;
    }(),
    bootstrap: {
        closeModal: function (target, callback) {
            setTimeout(function () {
                $(target).closest(".modal").modal("hide");
                if (callback) {
                    callback();
                }
            }, 250);
        }
    },
    knockout: {
        getValue: function (o) {
            return (typeof o === 'function' ? o() : o);
        }
    },
    validation: {
        setRequiredElementIndicator: function (element) {
            $(element).closest("div.form-group").addClass("required");
        },
        removeRequiredElementIndicator: function (element) {
            $(element).closest("div.form-group").removeClass("required");
        }
    },
    notifications: {
        showAlertDialog: function (title, message, options) {
            options = $.extend({}, {
                type: BootstrapDialog.TYPE_DEFAULT,
                buttons: [],
                showCloseButton: true,
                closeButtonText: "Cancel",
                cssClass: "oeca-alert"
            }, options);

            if (options.showCloseButton == true) {
                options.buttons.push({
                    label: options.closeButtonText,
                    action: function (dialogItself) {
                        dialogItself.close();
                    }
                });
            }

            BootstrapDialog.show({
                type: options.type,
                title: title,
                message: message,
                buttons: options.buttons,
                cssClass: options.cssClass
            });
        },
        showErrorMessage: function (msg, options, onHiddenFn) {
            options = $.extend({}, {
                title: "",
                timeOut: 5000,
                extendedTimeOut: 5000,
                closeButton: true,
                newestOnTop: true,
                showMethod: "slideDown",
                hideMethod: "slideUp",
                closeMethod: "slideUp",
                preventDuplicates: true,
                positionClass: "toast-bottom-right",
                onHidden: onHiddenFn
            }, options);

            return toastr.error(msg, options.title, options);
        },
        showSuccessMessage: function (msg, options) {
            options = $.extend({}, {
                title: "",
                timeOut: 5000,
                extendedTimeOut: 5000,
                closeButton: true,
                newestOnTop: true,
                showMethod: "slideDown",
                hideMethod: "slideUp",
                closeMethod: "slideUp",
                preventDuplicates: true,
                positionClass: "toast-bottom-right"

            }, options);

            return toastr.success(msg, options.title, options);
        },
        showWarnMessage: function (msg, options) {

            options = $.extend({}, {
                title: "",
                timeOut: 5000,
                extendedTimeOut: 5000,
                closeButton: true,
                newestOnTop: true,
                showMethod: "slideDown",
                hideMethod: "slideUp",
                closeMethod: "slideUp",
                preventDuplicates: true,
                positionClass: "toast-bottom-right"
            }, options);

            return toastr.warning(msg, options.title, options);
        },
        showInfoMessage: function (msg, options) {

            options = $.extend({}, {
                title: "",
                timeOut: 5000,
                extendedTimeOut: 5000,
                closeButton: true,
                newestOnTop: true,
                showMethod: "slideDown",
                hideMethod: "slideUp",
                closeMethod: "slideUp",
                preventDuplicates: true,
                positionClass: "toast-bottom-right"
            }, options);

            return toastr.info(msg, options.title, options);
        },
        messageTypes: {
            SUCCESS: "Success",
            INFO: "Info",
            WARNING: "Warning",
            ERROR: "Error"
        },
        showMessage: function (messageType, msg, options) {
            switch (messageType) {
                case oeca.notifications.messageTypes.SUCCESS:
                    oeca.notifications.showSuccessMessage(msg, options);
                    break;
                case oeca.notifications.messageTypes.INFO:
                    oeca.notifications.showInfoMessage(msg, options);
                    break;
                case oeca.notifications.messageTypes.WARNING:
                    oeca.notifications.showWarnMessage(msg, options);
                    break;
                case oeca.notifications.messageTypes.ERROR:
                    oeca.notifications.showErrorMessage(msg, options);
                    break;
                default:
                    alert("Invalid notification message type provided!");
            }
        },
        //OECA style messages
        successAlert: function(msg) {
            if(typeof msg == "string") {
                msg = {message: msg};
            }
            msg = $.extend({
                type: BootstrapDialog.TYPE_SUCCESS,
                title: '<span class="glyphicon glyphicon-ok-sign center-block"></span>',
                bodyTitle: 'Success!',
                message: msg,
                cssClass: "oeca-alert oeca-success-alert",
                buttons: [{
                    label: 'Ok',
                    action: function(dialogRef) {
                        dialogRef.close();
                    }
                }]
            }, msg);
            var dialog = new BootstrapDialog(msg);
            dialog.realize();
            if(msg.bodyTitle) {
                dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
            }
            if(msg.helpText) {
                dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
            }
            dialog.open();
            return dialog;
        },
        infoAlert: function(msg) {
            if(typeof msg == "string") {
                msg = {message: msg};
            }
            msg = $.extend({
                type: BootstrapDialog.TYPE_INFO,
                title: '<span class="glyphicon glyphicon-question-sign center-block"></span>',
                message: msg,
                cssClass: "oeca-alert oeca-info-alert",
                buttons: [{
                    label: 'Ok',
                    action: function(dialogRef) {
                        dialogRef.close();
                    }
                }]
            }, msg);
            var dialog = new BootstrapDialog(msg);
            dialog.realize();
            if(msg.bodyTitle) {
                dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
            }
            if(msg.helpText) {
                dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
            }
            dialog.open();
            return dialog;
        },
        errorAlert: function(msg) {
            if(typeof msg == "string") {
                msg = {message: msg};
            }
            msg = $.extend({
                type: BootstrapDialog.TYPE_DANGER,
                title: '<span class="glyphicon glyphicon-remove-sign center-block"></span>',
                message: msg,
                cssClass: "oeca-alert oeca-error-alert",
                buttons: [{
                    label: 'Ok',
                    action: function(dialogRef) {
                        dialogRef.close();
                    }
                }]
            }, msg);
            var dialog = new BootstrapDialog(msg);
            dialog.realize();
            if(msg.bodyTitle) {
                dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
            }
            if(msg.helpText) {
                dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
            }
            dialog.open();
            return dialog;
        }
    },
    blockUI: {
        show: function (msg) {
            var isBlocked = $(window).data("blockUI.isBlocked");
            if (isBlocked && isBlocked == 1) {
                return;
            }

            $.blockUI({
                message: msg,
                css: oeca.blockUI.settings.css,
                baseZ: 9000
            });
        },
        showWithDelay: function (msg, delay) {
            if (!delay) {
                delay = oeca.blockUI.settings.delayInterval;
            }
            if (oeca.blockUI.settings.enableDelay == true) {
                oeca.blockUI.timeoutInstance = setTimeout(function () {
                    oeca.blockUI.show(msg);
                }, delay);
            } else {
                oeca.blockUI.show(msg);
            }
        },
        hide: function () {
            if (oeca.blockUI.timeoutInstance) {
                clearTimeout(oeca.blockUI.timeoutInstance);
            }
            $.unblockUI();
        },
        settings: {
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .5,
                color: '#fff'
            },
            enableDelay: true,
            delayInterval: 3000
        }
    },
    xhrSettings: {
        mimeTypes: {
            JSON: "application/json",
            XML: "application/xml"
        },
        setJsonAcceptHeader: function (xhr) {
            xhr.setRequestHeader('Accept', oeca.xhrSettings.mimeTypes.JSON);
        },
        setXmlAcceptHeader: function (xhr) {
            xhr.setRequestHeader('Accept', oeca.xhrSettings.mimeTypes.XML);
        }
    },
    cookies: {
        getCookie: function (cookieName) {
            var cookie = Cookies.get(cookieName);
            if (!cookie) {
                console.error("Cookie does not exist with name " + cookieName);
                return null;
            }

            var cookieData = JSON.parse(cookie);
            return cookieData;
        },
        cookieExists: function(cookieName) {
            return Cookies.get(cookieName) ? true : false;
        },
        setCookie: function (cookieName, data, options) {
            Cookies.set(cookieName, data, options);
        },
        deleteCookie: function (cookieName) {
            Cookies.remove(cookieName);
        }
    },
    layout: {
        full: function() {
            $('#container').removeClass('container').addClass('container-fluid');
        },
        form: function() {
            $('#container').removeClass('container-fluid').addClass('container');
        }
    },
    resetModel: function (model) {
        for (var property in model) {
            if (model.hasOwnProperty(property)) {
                var obj = model[property];
                if (ko.isObservable(obj)) {
                    obj(null);
                }  else if (typeof obj === 'object'){
                    oeca.resetModel(obj);
                }
            }
        }
    },

    pruneEmptyObjects: function (obj) {
        return function prune(current) {
            _.forOwn(current, function (value, key) {
                if (_.isUndefined(value)) {
                    delete current[key];
                } else if (_.isNull(value)) {
                    delete current[key];
                } else if (_.isNaN(value)) {
                    delete current[key];
                }
                else if ((_.isString(value) && _.isEmpty(value))) {
                    delete current[key];
                } else if ((_.isObject(value) && Object.prototype.toString.call(value) === "[object Date]" )) {
                    if (!value) {
                        delete current[key];
                    }
                }
                else if ((_.isObject(value) && _.isEmpty(prune(value)))) {
                    delete current[key];
                }
            });
            // remove any leftover undefined values from the delete
            // operation on an array
            if (_.isArray(current)) _.pull(current, undefined);

            return current;

        }(_.cloneDeep(obj));  // Do not modify the original object, create a clone instead
    },
    cleanKoProperties: function (object) {
        for (var property in object) {
            if (property.indexOf("_") == 0) {
                delete object[property];
            }
        }

        delete object.errors;
        delete object.isValid;
        //adding some sugar
        return object;
    },
    createNamespace: function (namespace) {
        var nsparts = namespace.split(".");
        var parent = oeca;

        for (var i = 0; i < nsparts.length; i++) {
            var partname = nsparts[i];

            if (typeof parent[partname] === "undefined") {
                parent[partname] = {};
            }

            parent = parent[partname];
        }

        return parent;
    },
    utils: {
        scrollToElement: function(id) {
            $('html, body').animate({
                scrollTop: $(id).offset().top - 50
            }, 500);
        },
        scrollToTop: function() {
            $("html, body").animate({
                scrollTop: $('.panel-group').offset().top - 50
            }, "slow");
        },
        formatDateTime: function(date) {
            var unwrappedDate = ko.utils.unwrapObservable(date);
            return unwrappedDate ? moment(unwrappedDate).format('MM/DD/YYYY h:mm A') : '';
        },
        panelComputed: function(selectedPanel, panelName) {
            return ko.pureComputed({
                read: function() {
                    return selectedPanel() == panelName;
                },
                write: function(show) {
                    if(show) {
                        selectedPanel(panelName);
                    }
                    else if(selectedPanel() == panelName) {
                        selectedPanel(null);
                    }
                }
            })
        },
        sequence: 1,
        nextSequence: function() {
            return ++oeca.utils.sequence;
        },
        dispose: function(property) {
            try {
                if(property) {
                    if(property.dispose) {
                        property.dispose();
                    }
                }
            }
            catch (e) {
                if(console.log()) {
                    console.log("caught error while trying to dispose property");
                    console.log(property);
                    console.log(e);
                }
            }
        },
        disposeList: function(list) {
            try {
                ko.utils.arrayForEach(ko.utils.unwrapObservable(list), function(item) {
                    oeca.utils.dispose(item);
                });
            }
            catch (e) {
                if(console.log) {
                    console.log("caught error while trying to dispose list");
                    console.log(list);
                    console.log(e);
                }
            }
        },
        andList: function (list) {
            if (list.size == 0) {
                return '';
            }
            if (list.size == 1) {
                return list[0];
            }
            return list.slice(0, list.length - 1).join(', ') + ' and ' + list[list.length - 1];
        }
    },
    cromerr: {
        sequence: 1,
        createTransaction: function (callback) {
            var transactionId = ++oeca.cromerr.sequence;
            if (callback) {
                postal.channel('cromerr-widget').subscribe('success.' + transactionId, function (data, envelope) {
                    callback(data);
                });
            }
            return transactionId;
        },
        startTransaction: function (transactionId) {
            if (!transactionId) {
                throw "Transaction ID is required to start cromerr widget.  Call oeca.cromerr.createTransaction to get a Transaction ID.";
            }
            var cromerrButton = $('#cromerr-widget-init');
            cromerrButton.attr('data-cromerr-transaction-id', transactionId);
            cromerrButton.click();
        },
        disposeTransaction: function (transactionId) {
            var allWidgetSubscriptions = postal.subscriptions['cromerr-widget'];
            if (allWidgetSubscriptions) {
                var subscriptions = allWidgetSubscriptions['success.' + transactionId];
                if (subscriptions) {
                    for (var i = 0; i < subscriptions.length; ++i) {
                        subscriptions[i].unsubscribe();
                    }
                }
            }
        }
    },
    acl: {
        user: {
            /**
             * Returns currently logged in user's pending requests for a given entity type.  The edit type is defined in by
             * the baseUrl
             * @param baseUrl - url to the services for a particular entity type
             */
            pending: function(baseUrl) {
                return $.getJSON(baseUrl + 'pending', function(requests) {
                    console.log(requests)
                });
            },
            /**
             * Returns requests the user has made in the past for a given entity type.  The edit type is defined in by
             * the baseUrl
             * @param baseUrl url to the services for a particular entity type
             */
            history: function(baseUrl) {
                return $.getJSON(baseUrl + 'history', function(requests) {
                    console.log(requests);
                });
            }
        },
        entity: {
            /**
             * Returns pending requests for a particular entity.
             * @param baseUrl url to the services for a particular entity type
             * @param entityId id of the entity in the acl database
             */
            pending: function(baseUrl, entityId, userId) {
                return $.getJSON(baseUrl + entityId + '/pending' + (userId ? '?userId=' + userId : ''), function(requests) {
                    console.log(requests);
                });
            },
            /**
             * Returns historical requests for a particular entity.
             * @param baseUrl url to the services for a particular entity type
             * @param entityId id of the entity in the acl database
             */
            history: function(baseUrl, entityId) {
                return $.getJSON(baseUrl + entityId + '/history');
            },
            current: function(baseUrl, entityId, userId) {
                return $.getJSON(baseUrl + entityId + '/current' + (userId ? '?userId=' + userId : ''));
            },
            /**
             * Returns user IDs of the users that have a Sign permission for particular entities.
             * @param baseUrl url to the services for a particular entity type
             * @param entityIds an array of ids of the entities in the acl database
             */
            signatoryUsers: function(baseUrl, entityIds) {
                return $.ajax({
                    url: baseUrl + 'retrieveSignatoryUsers',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(entityIds)
                }).success(function(results) {
                    postal.channel('acl').publish('retrieveSignatoryUsers', {
                        results: results,
                        api: baseUrl
                    });
                });
            }
        },
        request: {
            /**
             * Submits requests for permission to one or more entities.  The server will return back a status of pending,
             * approved or rejected for each entity.
             * @param baseUrl url to the services for a particular entity type
             * @param requests requests to submit for approval
             */
            submit: function(baseUrl, requests) {
                return $.ajax({
                    url: baseUrl + 'request',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(requests)
                }).success(function(results) {
                    postal.channel('acl').publish('request', {
                        results: results,
                        api: baseUrl
                    });
                });
            },
            /**
             * Submits requests to be approved for an entity.  These requests may not exist (in the case of giving a user
             * access) or may be modified from the original request (add or remove permissions).
             * @param baseUrl url to the services for a particular entity type
             * @param requests requests to approve
             */
            approve: function(baseUrl, requests) {
                return $.ajax({
                    url: baseUrl + 'approve',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(requests, {ignore: ['manageUserCount']})
                }).success(function(results) {
                    postal.channel('acl').publish('approve', {
                        results: results,
                        api: baseUrl
                    })
                });
            },
            /**
             * Submits requests to reject.
             * @param baseUrl url to the services for a particular entity type
             * @param requests requests to reject
             */
            reject: function(baseUrl, requests) {
                return $.ajax({
                    url: baseUrl + 'reject',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(requests, {ignore: ['manageUserCount']})
                }).success(function(results) {
                    postal.channel('acl').publish('reject', {
                        results: results,
                        api: baseUrl
                    });
                });
            },
            /**
             * Requests to cancel.  This is used for when a user wants to cancel a request they previously made.  This will
             * cancel all permission requests for a particular entity.
             * @param baseUrl url to the services for a particular entity type
             * @param requests requests to cancel
             */
            cancel: function(baseUrl, requests) {
                return $.ajax({
                    url: baseUrl + 'cancel',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(requests)
                }).success(function(results) {
                    postal.channel('acl').publish('cancel', {
                        results: results,
                        api: baseUrl
                    })
                });
            },
            /**
             * Immediately grants permissions.
             * @param baseUrl url to the services for a particular entity type
             * @param permissions permissions to revoke
             * @returns {*}
             */
            grant: function(baseUrl, permissions) {
                return $.ajax({
                    url: baseUrl + 'grant',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(permissions, {ignore: 'requestStatus'})
                }).success(function(results) {
                    postal.channel('acl').publish('grant', {
                        results: ko.mapping.toJS(permissions),
                        api: baseUrl
                    });
                });
            },
            /**
             * Revokes existing permissions.
             * @param baseUrl url to the services for a particular entity type
             * @param permissions permissions to revoke
             * @returns {*}
             */
            revoke: function(baseUrl, permissions) {
                return $.ajax({
                    url: baseUrl + 'revoke',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(permissions, {ignore: ['requestStatus', 'manageUserCount']})
                }).success(function(results) {
                    postal.channel('acl').publish('revoke', {
                        results: ko.mapping.toJS(permissions),
                        api: baseUrl
                    });
                });
            },
            allPending: function(baseUrl) {
                return $.getJSON(baseUrl + 'all/pending');
            }
        },
        utils: {
            isRequest: function (request) {
                var id = parseProperty(request, 'id');
                return !id;
            },
            isPending: function (request) {
                if (oeca.acl.utils.isRequest(request)) {
                    return false;
                }
                var requestStatus = parseProperty(request, 'requestStatus');
                return requestStatus && requestStatus == 'Pending';
            },
            isCurrent: function (request) {
                if (oeca.acl.utils.isRequest(request)) {
                    return false;
                }
                var requestStatus = parseProperty(request, 'requestStatus');
                return requestStatus != 'Pending';
            },
            parsePermission: function(permission) {
                return parseProperty(permission, 'modulePermission.permissionType')
                        || parseProperty(permission, 'permissionType')
                        || ko.utils.unwrapObservable(permission)
            },
            /**
             * Takes in a permission and returns the config object from the permissionTypes property in an acl config.
             * @param permission string representing the value of the permission type
             * @param config custom acl config for application if should have an array of permissionTypes if this is
             * missing or permissionTypes is missing this will returned undefined.
             */
            lookupPermission: function(permission, config) {
                var unwrappedConfig = ko.utils.unwrapObservable(config);
                if (unwrappedConfig && unwrappedConfig.permissionTypes) {
                    return ko.observableArray(ko.utils.unwrapObservable(unwrappedConfig.permissionTypes))
                        .lookupByProp(oeca.acl.utils.parsePermission(permission), 'value');
                }
            },
            sortPermissions: function(permissions, config) {
                return ko.utils.unwrapObservable(permissions).sort(function(a,b) {
                    var aConfig = oeca.acl.utils.lookupPermission(a, config);
                    var aOrder = aConfig ? aConfig.order : aConfig;
                    var bConfig = oeca.acl.utils.lookupPermission(b, config);
                    var bOrder = bConfig ? bConfig.order : bConfig;
                    return (aOrder < bOrder) ? -1 : (aOrder > bOrder) ? 1 : 0;
                });
            },
            /**
             * Lookups up a permission in acl configuration and returns that permission as the label value in the config.
             * This is useful for displaying permissions to users and allowing for custom names to be used in the app.
             * @param permission either permission object or a string representing the permission value
             * @param config acl config from application if this is undefined or config.permissionTypes this will return
             * permission.
             * @returns {*} string value to display
             */
            permissionDisplay: function(permission, config) {
                var permissionType = oeca.acl.utils.parsePermission(permission);
                var permissionConfig = oeca.acl.utils.lookupPermission(permissionType, config);
                if(permissionConfig && permissionConfig.label) {
                    return ko.utils.unwrapObservable(permissionConfig.label);
                }
                return permissionType;
            },
            /**
             * loops through the list of permissions and returns an array of display values for those permissions.  This
             * will also sort the permissions based on the order provided in the config.
             * @param permissions
             * @param config
             * @returns {Array}
             */
            permissionsDisplay: function(permissions, config) {
                var permissionsDiplay = [];
                permissions = oeca.acl.utils.sortPermissions(permissions, config);
                ko.utils.arrayForEach(ko.utils.unwrapObservable(permissions), function(permission) {
                    permissionsDiplay.push(oeca.acl.utils.permissionDisplay(permission, config));
                });
                return permissionsDiplay;
            },
            permissionsDisplayAsString: function(permissions, config) {
                var a = oeca.acl.utils.permissionsDisplay(permissions, config);
                return a ? a.join(', ') : a;
            }
        }
    }
};
var AuthenticationWorkflow = function(data, params) {
	var self = this;
    if (self == undefined) {
        data = $.extend({
            userId: null,
            password: null
        }, data);
        ko.mapping.fromJS(data, {}, self);
    }
	self.userId.extend({required:true});
	self.password.extend({required: true});
	self.errors = ko.validation.group(self, {deep: true});
	self.errorMessage = ko.observable(null);
	self.login = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
			return;
		}
		self.authenticate().error(function(res) {
			var response = res.responseJSON;
			if(response) {
				self.errorMessage(response.message);
			}
			else {
				self.errorMessage("We have encountered an error");
			}
		});
	}
	self.authenticate = function() {
		throw "Workflow must override authenticate";
	}
}
var CdxRedirectWorkflow = function(data, params) {
    var self = this;
    if(data == null) {
        data = $.extend({
            userId: null,
            password: null
        }, data);
    }
    ko.mapping.fromJS(data, {}, self);
    AuthenticationWorkflow.apply(self);
    self.params = params;
    this.authenticate = function() {
        return $.ajax({
            url: config.auth.ctx + '/api/authentication/v1/public/authenticate/token/cdx',
            type: 'post',
            data: ko.mapping.toJSON(self),
            contentType: oeca.xhrSettings.mimeTypes.JSON,
            success: function(result, status, xhr) {
                window.location.href = xhr.getResponseHeader('Location');
            },
            error: function(res) {
                oeca.logger.debug(res);
            },
            statusCode: {
                500: function(res) {
                    var errorCode = res.responseJSON.code;
                    var errorMessage = res.responseJSON.message;
                    if (errorCode == "E_AccountExpired") {
                        self.errorMessage(self.errorMessage()
                            + " Please <a href="+config.cdx.forgotPassUrl+">visit CDX</a> to reset your password.")
                        //window.location.href = config.cdx.forgotPassUrl;
                    }
                    else if (errorCode == "E_AccountLocked") {
                        if (~errorMessage.toLowerCase().indexOf("email")) {
                            window.location.href = config.cdx.activateAccountUrl+self.userId().toUpperCase();
                        } else if (~errorMessage.toLowerCase().indexOf("password")) {
                            self.errorMessage(self.errorMessage()
                                + " To unlock the account and re-establish your password click the 'Forgot password' link below. " +
                                "If you have difficulties unlocking your account, please contact the Help Desk at (888) 890-1995.")
                        }
                    }

                }
            }
        });
    }
}
var NaasWorkflow = function(data, params) {
	var self = this;
    data = $.extend({
        userId: null,
        password: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
	AuthenticationWorkflow.apply(self);
	self.params = params;
	self.authenticate = function() {
		return $.ajax({
			url: config.auth.ctx + '/api/authentication/v1/public/authenticate',
			type: 'post',
			data: ko.mapping.toJSON(self),
			contentType: 'application/json',
			success: function(result, status, xhr) {
				params.success();
			},
            error: function(res) {
                oeca.logger.debug(res);
            },
            statusCode: {
                500: function(res) {
                    var errorCode = res.responseJSON.code;
                    var errorMessage = res.responseJSON.message;
                    if (errorCode == "E_AccountExpired") {
                        self.errorMessage(self.errorMessage()
                            + " Please <a href="+config.cdx.forgotPassUrl+">visit CDX</a> to reset your password.")
                        //window.location.href = config.cdx.forgotPassUrl;
                    }
                    else if (errorCode == "E_AccountLocked") {
                        if (~errorMessage.toLowerCase().indexOf("email")) {
                            window.location.href = config.cdx.activateAccountUrl+self.userId().toUpperCase();
                        } else if (~errorMessage.toLowerCase().indexOf("password")) {
                            self.errorMessage(self.errorMessage()
                                + " To unlock the account and re-establish your password click the 'Forgot password' link below. " +
                                "If you have difficulties unlocking your account, please contact the Help Desk at (888) 890-1995.")
                        }
                    }

                }
            }
		})
	}
}
//components
ko.components.register('sign-in', {
	viewModel: {
		viewModelSelector: function(params, componentInfo) {
			if(params.type == 'cdx-redirect') {
				return CdxRedirectWorkflow;
			}
			else if(params.type == 'naas') {
				return NaasWorkflow;
			}
			else {
				throw params.type + " is not a valid workflow.  Please provide the type parameter or correct it to a valid workflow.";
			}
		}
	},
	template: {
		url: config.ctx + '/components/sign-in.html'
	}
})
//component loaders
var asyncViewModelLoader = {
	loadViewModel: function(name, viewModelConfig, callback) {
	    if(viewModelConfig.viewModelClass) {
            var dependencies = viewModelConfig.loadDependencies ? viewModelConfig.loadDependencies() : null;
            $.when.apply($, dependencies).done(function () {
                callback(function (params, componentInfo) {
                    return new viewModelConfig.viewModelClass(null, params);
                });
            });
        }
        else {
            callback(null);
        }
	},
	loadTemplate: function(name, templateConfig, callback) {
		if(templateConfig.url) {
			$.get(templateConfig.url, null, function(data) {
				callback($.parseHTML(data));
			})
		}
		else {
			callback(null);
		}
	}
}
var viewModelSelectorLoader ={
	loadViewModel: function(name, viewModelConfig, callback) {
		if(viewModelConfig.viewModelSelector) {
			callback(function(params, componentInfo) {
				var Model = viewModelConfig.viewModelSelector(params, componentInfo);
				return new Model(null, params);
			});
		}
		else {
			callback(null);
		}
	}
}
var singleDatasourceAsyncViewModelLoader = {
	loadViewModel: function(name, viewModelConfig, callback) {
		if(viewModelConfig.sharedData && viewModelConfig.viewModelClass) {
			callback(function(params, componentInfo) {
				var model = new viewModelConfig.viewModelClass(viewModelConfig.sharedData, params);
				return model;
			});
		}
		else {
			callback(null);
		}
	}
}
var modalLoader = {
	loadViewModel: function(name, viewModelConfig, callback) {
		if(viewModelConfig.modal) {
			callback(function(params, componentInfo) {
				popupId = params.id || name;
				popupRegistry[popupId] = new ModalControl(null);
				return popupRegistry[popupId];
			});
		}
		else {
			callback(null);
		}
	},
	loadTemplate: function(name, templateConfig, callback) {
		if(templateConfig.modal) {
			$.get(config.ctx + '/action/modal/' + templateConfig.modal, function(data) {
				callback($.parseHTML(data));
			});
		}
		else {
			callback(null);
		}
	}
}

ko.components.loaders.unshift(asyncViewModelLoader);
ko.components.loaders.unshift(viewModelSelectorLoader);
ko.components.loaders.unshift(singleDatasourceAsyncViewModelLoader);
ko.components.loaders.unshift(modalLoader);

//enable modal display on right-click for a "pager" page
var afterPageDisplay = function () {
    $("[data-toggle='modal']").on("contextmenu", function (e) {
        e.preventDefault();
        var targetModal = $(this).data('target');
        $(targetModal).modal("show");
    });
}
