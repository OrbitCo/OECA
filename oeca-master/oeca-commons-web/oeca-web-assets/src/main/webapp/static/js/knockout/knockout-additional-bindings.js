/**
 * Knockout binding handler for bootstrapSwitch indicating the status
 * of the switch (on/off): https://github.com/nostalgiaz/bootstrap-switch
 */
ko.bindingHandlers.bootstrapSwitchOn = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        $elem = $(element);
        $(element).bootstrapSwitch();
        $(element).bootstrapSwitch('state', ko.utils.unwrapObservable(valueAccessor())); // Set intial state
        // Update the model when changed.
        $elem.on('switchChange.bootstrapSwitch', function (e, state) {
            valueAccessor()(state);
        });
    },
    update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var vStatus = $(element).bootstrapSwitch('state');
        //The unwrap observable line below is causing multiple switches to display on browser back while using pagerjs
        //ToDo: debug why this is happening
        var vmStatus = ko.utils.unwrapObservable(valueAccessor());
        if (vStatus != vmStatus) {
            //remember if it was read only or not
            var readonlyStatus= $(element).bootstrapSwitch("disabled");
            if (readonlyStatus == true){
                $(element).bootstrapSwitch("disabled", false);
            }
            $(element).bootstrapSwitch('state', vmStatus);

            if (readonlyStatus == true){
                $(element).bootstrapSwitch("disabled", true);
            }
        }
    }
};

/**
 * Additional binding to deal with read only
 */
ko.bindingHandlers.bootstrapSwitchReadOnly = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var value = ko.unwrap(valueAccessor());
        $(element).bootstrapSwitch("disabled", value);
    },
    update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var value = ko.unwrap(valueAccessor());
        $(element).bootstrapSwitch("disabled", value);
    }
};

ko.bindingHandlers.datepicker = {
    init: function (element, valueAccessor, allBindingsAccessor) {
        //initialize datepicker with some optional options
        var options = allBindingsAccessor().datepickerOptions || { autoclose: true };
        $(element).datepicker(options);
    },
    update: function (element, valueAccessor) {
        var widget = $(element).data("datepicker");

        //when the view model is updated, update the widget
        if (widget) {
            var value = ko.utils.unwrapObservable(valueAccessor());
            if (value) {
                var dt = moment(value).toDate();
                $(element).datepicker('update', dt);
            } else {
                $(element).datepicker('update');
            }
        }
    }
};

ko.bindingHandlers.combodate = {
    init: function (element, valueAccessor, allBindingsAccessor) {
        var options = allBindingsAccessor().combodateOptions || {};
        $(element).combodate(options);
    },
    update: function (element, valueAccessor) {
        var widget = $(element).data("combodate");
        if (widget) {
            var value = ko.utils.unwrapObservable(valueAccessor());
            if (value) {
                var dt = moment(value).toDate();
                $(element).combodate('update', dt);
            } else {
                $(element).combodate('update');
            }
        }
    }
};

function forEach(array, callback) {
    for (var index = 0; index < array.length; ++index) {
        callback(array[index]);
    }
}

ko.bindingHandlers.tokenfield = {
    init: function (element, valueAccessor, allBindingsAccessor) {
        var getValue = function (o) {
            return (typeof o === 'function' ? o() : o);
        };
        var prefix = "tagForCode_";
        var idPrefix = "#" + prefix;
        var tokenListLookup = getValue(allBindingsAccessor().tokenListLookup);
        var listOfSelectedItems = valueAccessor() || {};

        var setTokenFieldPopover = function (codeValue) {
            if (tokenListLookup) {
                var lookupList = getValue(tokenListLookup.values);

                var obj = ko.utils.arrayFirst(lookupList, function(item) {
                    return getValue(item[tokenListLookup.key]) == codeValue;
                });
                var description = getValue(obj[tokenListLookup.description]);
                if (description) {
                    var $token = $(idPrefix + codeValue);
                    $token.data("content", description);
                    $token.popover({trigger: "hover"});
                }
            }
        };

        var destroyTokenFieldPopover = function (codeValue) {
            var $token = $(idPrefix + codeValue);
            $token.popover('destroy');
        };

        //initialize tokenfield with some optional options
        var tokenFieldElement = $(element).tokenfield();
        //this will take care of stuff
        var tokenFieldContext = tokenFieldElement.parent();
        //disable the input
        $(".token-input", tokenFieldContext).attr("disabled", "disabled");

        tokenFieldElement.on('tokenfield:createdtoken', function (e) {
            var value = e.attrs.value;
            e.relatedTarget.id = prefix + value;
            setTokenFieldPopover(value);
        }).on('tokenfield:removedtoken', function (e) {
            var value = e.attrs.value;
            destroyTokenFieldPopover(value);
            listOfSelectedItems.remove(value);
        });

        tokenFieldElement.tokenfield('setTokens', ko.utils.unwrapObservable(listOfSelectedItems));

        tokenFieldElement.on('tokenfield:removetoken', function (e) {
            listOfSelectedItems.remove(ko.utils.arrayFirst(listOfSelectedItems(), function(item) {
                return item == e.attrs.value;
            }));
        });

        // Subscribe to the changes in the ko.observableArray
        listOfSelectedItems.subscribe(function (changes) {
            forEach(changes, function (change) {
                switch (change.status) {
                    case 'added':
                        tokenFieldElement.tokenfield('createToken', {value: change.value, label: change.value});
                        setTokenFieldPopover(change.value);
                        break;
                    case 'deleted':
                        destroyTokenFieldPopover(change.value);
                        $(idPrefix + change.value, tokenFieldContext).remove();
                        break;
                }
            });
        }, null, "arrayChange");


    },
    update: function (element, valueAccessor) {
    }
};

ko.bindingHandlers.foreachprop = {
    transformObject: function (obj) {
        var properties = [];
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                properties.push({ key: key, value: obj[key] });
            }
        }
        return properties;
    },
    init: function(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        return ko.bindingHandlers.foreach.init(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext);
    },
    update: function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {

        var value = ko.utils.unwrapObservable(valueAccessor()),
            properties = ko.bindingHandlers.foreachprop.transformObject(value);

        var newValue = function () {
            return {
                data: properties
            };
        };

        ko.bindingHandlers.foreach.update(element, newValue, allBindingsAccessor, viewModel, bindingContext);
        return { controlsDescendantBindings: true };
    }
};

ko.bindingHandlers.date = {
    init: function (element, valueAccessor, allBindingsAccessor) {
        var formats = allBindingsAccessor().dateFormats || {from: "", to: ""};
        element.onchange = function () {
            var observable = valueAccessor();

            if (element.value) {
                var momentDate = moment(new Date(element.value));
                //if format is not set then assume observed is a js date
                if (formats.from) {
                    observable(momentDate.format(formats.from));
                }
                else {
                    observable(momentDate.toDate());
                }
            }
            else {
                observable(null);
                //ensures element is blank when invalid input is attempted
                if (element.value) element.value = "";
            }
        };
    },
    update: function (element, valueAccessor, allBindingsAccessor) {
        var formats = allBindingsAccessor().dateFormats || {from: "", to: "MM/DD/YYYY"};
        var observable = valueAccessor();
        var valueUnwrapped = ko.utils.unwrapObservable(observable);
        if (valueUnwrapped) {
            ko.bindingHandlers.text.update(element, function () {
                var ret = moment(valueUnwrapped).format(formats.to);

                return ret;
            });
            element.value = moment(valueUnwrapped).format(formats.to);
        }
        else {
            element.value = "";
        }
    }
};


ko.bindingHandlers.userInfo = {

    init: function (element, valueAccessor, allBindingsAccessor) {

    },
    update: function (element, valueAccessor, allBindingsAccessor) {
        // First get the latest data that we're bound to
        var value = valueAccessor();

        // Next, whether or not the supplied model property is observable, get its current value
        var user = ko.unwrap(value);
        var fullName = [user.contact.firstName()];
        if (user.contact.middleInitial() != undefined && user.contact.middleInitial() != '') {
            fullName.push(user.contact.middleInitial())
        }
        fullName.push(user.contact.lastName());
        fullName.push("<" + user.userId() + ">");
        $(element).text(fullName.join(' '));
    }
};

ko.bindingHandlers.zipAutofill = {
    init: function (element, valueAccessor) {
        var options = $.extend({}, {autofillOnStart: false}, valueAccessor()),
            autofillOnStart = options.autofillOnStart,
            cityId = options.city.id,
            city = options.city.observable,
            state = options.state,
            country = options.country,
            disableState = options.disableState ? options.disableState : false,
            stateListOptions = options.stateList,
            countryListOptions = options.countryList,
            allowableStateCode = options.allowableStateCode,
            validationMessageClass = "warningValidationMessage",
            request;

        var messageContainer = $('<span class="' + validationMessageClass + '"></span>');

        if (allowableStateCode) {
            $(element).after(messageContainer).hide();
        }

        var getZipLocation = function (newValue) {
            if (request) {
                request.abort();
            }

            $(messageContainer).text('').hide();

            if (!newValue) {
                return;
            }
            request = $.ajax({
                url: options.url + "/" + newValue,
                method: "GET",
                global: false,
                success: invokeAutofill
            });
        };

        var setCountryUS = function () {
            if (countryListOptions) {
                var countryList = getValue(countryListOptions);

                $.each(countryList, function (i, v) {
                    if (getValue(v.code) == "US") {
                        country.id(getValue(v.id));
                    }
                });
            }
        };

        var invokeAutofill = function (locations) {
            destroyCityDropdown();

            if (locations.length == 1) {
                if (allowableStateCode && locations[0].state != allowableStateCode) {
                    var message = "This Zip Code is associated with a state different than " + allowableStateCode + ".";
                    $(messageContainer).text(message).show();
                    return;
                }
                setCountryUS();
                setTimeout(function() {setLocation(locations[0]);}, 250);
            } else if (locations.length > 1) {
                //Create dropdown with selections
                buildCityDropdown(locations);
                setCountryUS();
            }
        };

        var buildCityDropdown = function (locations) {
            var $cityInputElem = $(cityId),
                $citySelectElem = $("<select/>", {
                    id: $cityInputElem.attr("id") + "_select",
                    class: $cityInputElem.attr('class')
                }),
                $editToggleElem = $("<span/>", {
                    class: "pull-right"
                }).append($("<a/>", {
                    href: "#",
                    class: "small"
                }).text("Edit"));

            $('<option />', {value: "", text: "Select a City"}).appendTo($citySelectElem);
            ko.utils.arrayForEach(locations, function (data) {
                var $selectOption = $('<option />', {value: data.city, text: data.city})
                    .data("location", data);

                if (city() && data.city.toLowerCase() == city().toLowerCase()) {
                    $selectOption.prop('selected', true);
                }

                $selectOption.appendTo($citySelectElem);
            });

            var $selectedLocation = $('option:selected', $citySelectElem).data("location");

            if (!city() || !$selectedLocation) {
                $citySelectElem.addClass("error").insertAfter(cityId);
            }

            $cityInputElem.hide();
            $citySelectElem.insertAfter(cityId);
            $citySelectElem.after($editToggleElem);

            $citySelectElem.change(function () {
                if ($(this).val() == "") {
                    $citySelectElem.addClass("error");
                } else {
                    $citySelectElem.removeClass("error");
                }

                setLocation($('option:selected', this).data("location"));
            });

            $editToggleElem.click(function (evt) {
                evt.preventDefault();
                destroyCityDropdown();
                $cityInputElem.focus();
                return false;
            });

            setLocation($selectedLocation);
        };

        var destroyCityDropdown = function () {
            var $cityInputElem = $(cityId),
                $citySelectElem = $(cityId + "_select"),
                $editToggleElem = $citySelectElem.next();

            $citySelectElem.remove();
            $editToggleElem.remove();
            $cityInputElem.show();
        };

        var getValue = function (o) {
            return (typeof o === 'function' ? o() : o);
        };

        var setLocation = function (data) {
            if (!data) {
                if (disableState == false) {
                    if (typeof state == "function") {
                        state(null);
                    } else {
                        for (var property in state) {
                            if (state.hasOwnProperty(property)) {
                                state[property](null);
                            }
                        }
                    }
                }
                city(null);
                return;
            }

            if (disableState == false && data.state) {
                if (stateListOptions) {
                    var stateList = getValue(stateListOptions.data);

                    $.each(stateList, function (i, v) {
                        if (getValue(v[stateListOptions.key]) == getValue(data.state)) {
                            if (typeof state == "function") {
                                state(v);
                            } else {
                                var source = ko.mapping.toJS(v);

                                state.id(source.id);
                                state.code(source.code);
                                state.description(source.description);
                            }
                        }
                    });
                } else {
                    state(data.state);
                }
            }


            if (data.city) {
                city(data.city);
            }
        };

        //detect whenever the zip value changes
        options.zip.subscribe(getZipLocation);

        if (autofillOnStart && options.zip()) {
            getZipLocation(options.zip());
        }
    }
};

ko.bindingHandlers.inputmask = {
    init: function (element, valueAccessor, allBindingsAccessor) {
        //initialize datepicker with some optional options
        var options = allBindingsAccessor().inputmaskOptions || {};
        $(element).inputmask(options);
    },
    update: function (element, valueAccessor) {
        var widget = $(element).data("inputmask");

        //when the view model is updated, update the widget
        if (widget) {
            var value = ko.utils.unwrapObservable(valueAccessor());
            $(element).val(value());
        }
    }
};

ko.bindingHandlers.maskedInput = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel, mask) {
        if (allBindingsAccessor.get('mask')) {
            mask = allBindingsAccessor.get('mask');
        }

        var value = valueAccessor();
        if (ko.isObservable(value)) {
            $(element).on('focusout change', function () {
                if ($(element).inputmask('isComplete')) {
                    value($(element).val());
                } else {
                    value(null);
                }
            });
        }
        $(element).inputmask({mask: mask, greedy: false});
        ko.bindingHandlers.value.init(element,valueAccessor, allBindingsAccessor);
    },
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        value();
    }
};

ko.bindingHandlers.maskedPhone = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask || "999-999-9999";
        ko.bindingHandlers.maskedInput.init(element, valueAccessor, allBindingsAccessor, viewModel, mask);
    },
    update: function (element, valueAccessor) {
        ko.bindingHandlers.maskedInput.update(element, valueAccessor);
    }
};

ko.bindingHandlers.maskedPhoneExtension = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask || "[9999]";
        ko.bindingHandlers.maskedInput.init(element, valueAccessor, allBindingsAccessor, viewModel, mask);
    },
    update: function (element, valueAccessor) {
        ko.bindingHandlers.maskedInput.update(element, valueAccessor);
    }
};

ko.bindingHandlers.maskedZip = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask || "99999[-9999]";
        ko.bindingHandlers.maskedInput.init(element, valueAccessor, allBindingsAccessor, viewModel, mask);
    },
    update: function (element, valueAccessor) {
        ko.bindingHandlers.maskedInput.update(element, valueAccessor);
    }
};

ko.bindingHandlers.handlerId = {
    init: function (element, valueAccessor, allBindingsAccessor) {},
    update: function (element, valueAccessor, allBindingsAccessor) {
        var value = valueAccessor();

        var handlerId = ko.unwrap(value);
        var yesNo = handlerId ? handlerId : "NOT YET ASSIGNED";

        $(element).text(yesNo);
    }
};

ko.bindingHandlers.yesNo = {

    init: function (element, valueAccessor, allBindingsAccessor) {

    },
    update: function (element, valueAccessor, allBindingsAccessor) {
        var value = valueAccessor();

        var bool = ko.unwrap(value);
        var yesNo = bool == true ? "Yes" : "No";

        $(element).text(yesNo);
    }
};

/**
 * Prints 'Yes' if the value evaulates to true other wise if the value is false it prints 'No' and nothing if the value
 * is null or undefined.
 * @type {{}}
 */
ko.bindingHandlers.yesNoBlank = {
    update: function(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        var value = valueAccessor();
        return ko.bindingHandlers.text.update(element, function() {
            if(ko.utils.unwrapObservable(value)) {
                return 'Yes';
            }
            else if (ko.utils.unwrapObservable(value) == false) {
                return 'No';
            }
            return '';
        }, allBindingsAccessor, viewModel, bindingContext);
    }
}

ko.bindingHandlers.yOrBlank = {
    init: function (element, valueAccessor, allBindingsAccessor) {

    },
    update: function (element, valueAccessor, allBindingsAccessor) {
        var value = valueAccessor();

        var bool = ko.unwrap(value);
        var yesNo = (bool == true) ? "Y" : "";

        $(element).text(yesNo);
    }
};


//Knockout Validation Manual Server-Side Validation Messages
ko.bindingHandlers.validatableSectionMessage = {
    init: function (element, valueAccessor, allBindingsAccessor) {
        var observable = allBindingsAccessor().text;
        observable.originalValue = observable();

        if (typeof observable != "undefined" && (observable() === null || observable() === "")) {
            $(element).addClass("hidden");
        }
    },
    update: function (element, valueAccessor, allBindingsAccessor) {
        var observable = allBindingsAccessor().text;

        if (observable() != undefined && observable() != null && observable() != "") {
            $(element).addClass('validationMessage');
            var section = $('#' + $(element).data('messageFor'));

            section.addClass('error');
        }
    }
};

ko.showValidationMessages = function (errors, context) {
    if (!errors) {
        return;
    }

    $.each(errors, function (i, error) {
        var path = 'context.' + error.key;
        var ref = eval(path);

        if (error.type === "Field") {
            ref.setError(error.message);
            ref.isModified(true);
        } else if (error.type === "Section") {
            ref(error.message);
        }
    });
};

ko.registerValidatableField = function (field) {
    if (ko.isObservable(field) && !ko.isComputed(field)) {
        field.extend({validatable: true});
    }
};

ko.registerValidatableFields = function (arr) {
    for (var idx in arr) {
        var field = arr[idx];
        ko.registerValidatableField(field);
    }
};

ko.registerAllValidatableFields = function (obj) {
    if (obj) {
        for (var key in obj) {
            var field = obj[key];

            if (ko.isObservable(field) && !ko.isComputed(field)) {
                if (typeof field() == "object" && field() != null) {
                    ko.registerAllValidatableFields(field());
                } else {
                    ko.registerValidatableField(field);
                }
            } else if (typeof field == "object") {
                ko.registerAllValidatableFields(field);
            }
        }
    }

    return;
};

ko.dirtyFlag = function(root, isInitiallyDirty, settings) {
    if(settings && settings.initDirty) {
        isInitiallyDirty = true;
    }
    var result = function() {},
        _initialState = ko.observable(ko.mapping.toJSON(root, settings)),
        _isInitiallyDirty = ko.observable(isInitiallyDirty);

    result.isDirty = ko.computed(function() {
        return _isInitiallyDirty() || _initialState() !== ko.mapping.toJSON(root, settings);
    });

    result.reset = function() {
        _initialState(ko.mapping.toJSON(root, settings));
        _isInitiallyDirty(false);
    };

    result.debug = function() {
        console.log("settings");
        console.log(settings);
        console.log("original data");
        console.log(_initialState());
        console.log("current data");
        console.log(ko.mapping.toJSON(root, settings));
        console.log("is dirty? " + result.isDirty());
    }

    return result;
};

/*
 * Extenders
 */
/**
 * Pads the value to given length.  You provide the length you would like your string to be as the value and this will
 * pad it with 0s.  Otherwise you can provide an object to set other parameters.  Example options:
 * {
 *      padLength: 3,
 *      padString: '0'
 * }
 *
 * Note: this will not prevent your string from being longer than the length provided.  If the length of the string
 * exceeds the length passed in this will return the string passed in.
 *
 * @param target
 * @param option
 * @returns {KnockoutComputed<T>|KnockoutSubscribable<T>}
 */
ko.extenders.pad = function(target, option) {
    var length = option.padLength || option;
    if(length < 0) {
        throw "Length cannot be negative";
    }
    var padString = option.padString || '0';
    var paddedString = '';
    for(var i = 0; i < length; ++i) {
        paddedString += String(padString);
    }
    var computed = ko.pureComputed({
        read: target,
        write: function(newVal) {
            var formatted = newVal ? oeca.common.utils.padWithPaddedString(newVal, length, paddedString) : null;
            if(formatted !== target()) {
                target(formatted);
            }
            else if (newVal !== target()) {
                //value was changed but formatted the same notify subscribers.
                target.notifySubscribers(formatted);
            }
        }
    }).extend({notify: 'always'});
    computed(target());
    return computed;
};
/**
 * Rounds value to the specified precision.  You may pass in a number as the value to the extender and that number will
 * be used as the precision or you may pass in some options in the format of:
 * {
 *      precision: 2,
 *      decimal: true
 * }
 * options:
 * precision - specifies to what digit to round to
 * decimal - if true the precision will round to the decimal digit otherwise it will round to the whole number digit.
 *
 * Examples:
 * var observable = ko.observable(null).extend({round: 2});
 * observable(1234.56789)
 * observable will be 1234.57
 * ---------
 * var observable = ko.observable(null).extend({round: {precision: 2, decimal: false}});
 * observable(1234.56789)
 * observable will be 1200.
 *
 * @param target
 * @param option
 * @returns {KnockoutComputed<T>|KnockoutSubscribable<T>}
 */
ko.extenders.round = function(target, option) {
    if(option == null || option == undefined) {
        throw "You must provide a value for the round extender"
    }
    var precision = option.precision == null || option.precision == undefined ? option : option.precision;
    if(precision == null || precision == undefined || isNaN(precision)) {
        throw "Precision is required and must be a number for the round extender"
    }
    var decimal = option.decimal == null || option.decimal == undefined ? true : option.decimal;
    var scale = Math.pow(10, precision);
    var computed = ko.pureComputed({
        read: target,
        write: function(newVal) {
            if(newVal == null || newVal == undefined || newVal == '') {
                if(target() == newVal) {
                    return;
                }
                else {
                    target(newVal);
                    return;
                }
            }
            var rounded = null;
            if(scale == 0) {
                //avoid divide by 0
                rounded = Math.round(newVal);
            }
            else {
                var rounded = decimal ? Math.round(newVal * scale)/scale : Math.round(newVal/scale) * scale;
            }
            if(rounded !== target()) {
                target(rounded);
            }
            else if(newVal !== target()) {
                //value was changed but formatted the same notify subscribers.
                target.notifySubscribers(rounded);
            }
        }
    }).extend({notify: 'always'});
    computed(target());
    return computed;
};

ko.extenders.async = function(computedDeferred, initialValue) {
    var plainObservable = ko.observable(initialValue), currentDeferred;
    plainObservable.inProgress = ko.observable(false);

    ko.computed(function() {
        if (currentDeferred) {
            currentDeferred.reject();
            currentDeferred = null;
        }

        var newDeferred = computedDeferred();
        if (newDeferred && (typeof newDeferred.done == "function")) {
            plainObservable.inProgress(true);
            currentDeferred = $.Deferred().done(function(data) {
                plainObservable.inProgress(false);
                plainObservable(data);
            });
            newDeferred.done(currentDeferred.resolve);
        } else {
            plainObservable(newDeferred);
        }
    });

    return plainObservable;
};

//Extender to defer validation
ko.extenders.deferValidation = function (target, option) {
       if (option) {
           target.subscribe(function(){
               target.isModified(false);
           });
       }
       return target;
};
/**
 * Initializes the table as a data table. Takes in an object with the options to
 * pass in to initialize the table.
 */
ko.bindingHandlers.datatable = {
    init: function (element, valueAccessor, allBindings) {
        var options = ko.utils.unwrapObservable(valueAccessor());
        var row = $(element).closest('table').find('tbody > tr');
        row.remove();
        var table = $(element).closest('table').DataTable(options);
        //TODO hack to remove problem with child tables figure out how to customize the css used in the wrapper element.
        $(element).closest('.dataTables_wrapper').removeClass('form-inline');
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            table.destroy();
        });
    }
}
/**
 * Hooks up an observable array to the datatable data source. Adding or removing
 * from the array will add or remove the item from the data table array.
 */
ko.bindingHandlers.datasource = {
    init: function (element, valueAccessor, allBindings) {
        var data = ko.utils.unwrapObservable(valueAccessor());
        var table = $(element).closest('table').DataTable();
        var refresh = function () {
            var data = ko.utils.unwrapObservable(valueAccessor());
            var rowsToRemove = [];
            table.rows().every(function (rowIndex) {
                var row = table.row(rowIndex);
                if (data.indexOf(row.data()) == -1) {
                    rowsToRemove.push(rowIndex);
                }
            });
            table.rows(rowsToRemove).remove()
            for (var i = 0; i < data.length; ++i) {
                var item = data[i];
                if (item && table.data().indexOf(item) == -1) {
                    table.row.add(item);
                }
            }
            table.draw();
        }
        refresh();
        var subscription;
        if (valueAccessor().subscribe) {
            subscription = valueAccessor().subscribe(function () {
                refresh();
            });
        }
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            if (subscription) {
                subscription.dispose();
            }
        });
        return {controlsDescendantBindings: true};
    }
}
/**
 * Renders the template passed in as a child row for each row in the table. In
 * order for this to work you will need an element with the class
 * 'details-control' this will update a span to toggle between a plus and minus
 * for when the row is shown and hidden. This element acts as the click tigger
 * to hide/show the child row.
 */
ko.bindingHandlers.childRow = {
    init: function (element, valueAccessor, allBindings) {
        var template = ko.utils.unwrapObservable(valueAccessor());
        var data = null;
        var vm = null;
        var allowMultiple = true;
        if (typeof template != 'String') {
            data = template.data;
            vm = template.vm;
            allowMultiple = template.allowMultiple == undefined ? true : template.allowMultiple;
            template = template.name;
        }
        var table = $(element).closest('table').DataTable();
        var templateHtml = $('#' + template).html();
        $(element).on('click', '.details-control', function () {
            var table = $(element).closest('table').DataTable();
            var row = table.row($(this).closest('tr'));
            var applyBindings = false;
            if (!row.child()) {
                row.child(templateHtml);
                applyBindings = true;
            }
            var elem = $(this);
            var hideRow = function(row, elem) {
                row.child.hide();
                elem.children('span.glyphicon').removeClass("glyphicon-minus-sign");
                elem.children('span.glyphicon').addClass('glyphicon-plus-sign');
            }
            var showRow = function(row, elem) {
                if(data && allowMultiple == false) {
                    $(element).find('tr > td.details-control').each(function() {
                        hideRow(table.row(this), $(this));
                    });
                }
                row.child.show();
                elem.children('span.glyphicon').addClass('glyphicon-minus-sign');
                elem.children('span.glyphicon').removeClass('glyphicon-plus-sign');
            }
            if (row.child.isShown()) {
                hideRow(row, elem);
            }
            else {
                showRow(row, elem);
            }
            if (applyBindings) {
                //delay applying bindings until after the row is shown so the map shows up
                var closeChildRowFunc = function() {
                    hideRow(row, elem);
                }
                var ChildRowVm = vm || function (data) {
                        var self = this;
                        ko.mapping.fromJS(data, {}, self);
                        self.closeChildRow = closeChildRowFunc;
                    }
                ko.applyBindings(new ChildRowVm($.extend({
                    data: row.data(),
                    closeChildRow: closeChildRowFunc
                }, data)), $(row.child()[0]).get(0));
            }
        });
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            $(element).off();
        });
    }
}
/**
 * uses data tables api to select rows based on the objects in the passed in observable array.  This does an address
 * comparision with the data used for each row in the table.  This does not provide any functionality to add/remove rows
 * to the selected list.
 * @type {{update: ko.bindingHandlers.selectedRows.update}}
 */
ko.bindingHandlers.selectedRows = {
    update: function(element, valueAccessor, allBindingAccessor, viewModel, bindingContext) {
        var selectedRows = ko.utils.unwrapObservable(valueAccessor());
        var dt = $(element).closest('table').DataTable();
        dt.rows().every(function(index){
            var row = dt.row(index);
            row.select(selectedRows.contains(row.data()));
        });
    }
};
/**
 * On click this will will get the table row the event occurred in, grab the data from datatable for that row and push
 * it into the selected list.  This is a simple binding that works well with selectedRows and be used on a button or row
 * to all the user to select rows from a table.
 * @type {{init: ko.bindingHandlers.selectRow.init}}
 */
ko.bindingHandlers.selectRow = {
    init: function(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        var selectedRows = ko.utils.unwrapObservable(valueAccessor());
        var dt = $(element).closest('table').DataTable();
        var row = dt.row(element);
        ko.bindingHanglers.click.init(element, function(){
            return function() {
                if(selectedRows.contains(row.data())) {
                    valueAccessor().remove(row.data());
                }
                else {
                    selectedRows.push(row.data());
                }
            };
        })
    }
};
/**
 * Old binding recomended you use the datatable and datasource bindings instead.  This binding foreaches through your
 * loop to create a table and initializes the table after building all the rows.  If any changes occur to the value it
 * will destroy the table apply the changes then reinitialize the table.
 * @type {{page: number, init: ko.bindingHandlers.dataTablesForEach.init, update: ko.bindingHandlers.dataTablesForEach.update}}
 */
ko.bindingHandlers.dataTablesForEach = {
	page: 0,
    init: function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
      var options = ko.unwrap(valueAccessor());
      ko.unwrap(options.data);
      if(options.dataTableOptions.paging){
    	valueAccessor().data.subscribe(function (changes) {
        	var table = $(element).closest('table').DataTable();
            ko.bindingHandlers.dataTablesForEach.page = table.page();
            table.destroy();
        }, null, 'arrayChange');
      }
        var nodes = Array.prototype.slice.call(element.childNodes, 0);
        ko.utils.arrayForEach(nodes, function (node) {
        	if (node && node.nodeType !== 1) {
            	node.parentNode.removeChild(node);
            }
        });
        return ko.bindingHandlers.foreach.init(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext);
    },
    update: function (element, valueAccessor, allBindings, viewModel, bindingContext) {
        var options = ko.unwrap(valueAccessor()),
            key = 'DataTablesForEach_Initialized';
        ko.unwrap(options.data);
        var table;
        if(!options.dataTableOptions.paging){
          table = $(element).closest('table').DataTable();
        	table.destroy();
        }
        ko.bindingHandlers.foreach.update(element, valueAccessor, allBindings, viewModel, bindingContext);
        table = $(element).closest('table').DataTable(options.dataTableOptions);
        if (options.dataTableOptions.paging) {
           if (table.page.info().pages - ko.bindingHandlers.dataTablesForEach.page == 0)
               table.page(--ko.bindingHandlers.dataTablesForEach.page).draw(false);
           else
               table.page(ko.bindingHandlers.dataTablesForEach.page).draw(false);
        }
        if (!ko.utils.domData.get(element, key) && (options.data || options.length))
            ko.utils.domData.set(element, key, true);
        return { controlsDescendantBindings: true };
    }
};
/**
 * Allows you to pass in an observable as the data for a datatable column. This
 * will unwrap the observable and update the cell in datatable whenever the data
 * changes.
 */
$.fn.dataTable.render.ko = {
    observable: function () {
        return function (data, type, full, meta) {
            if (ko.isObservable(data)) {
                var subscription = data.subscribe(function (newVal) {
                    $(meta.settings.nTable).DataTable().cell(meta.row, meta.col).invalidate();
                })
                $(meta.settings.nTable).on("destroy.dt", function () {
                    subscription.dispose();
                });
                return data();
            }
            return data;
        }
    },
    computed: function (func) {
        return function (data, type, full, meta) {
            var computed = ko.pureComputed(function () {
                return func(data, type);
            });
            var subscription = computed.subscribe(function () {
                $(meta.settings.nTable).DataTable().cell(meta.row, meta.col).invalidate();
            });
            $(meta.settings.nTable).on("destroy.dt", function () {
                subscription.dispose();
                computed.dispose();
            });
            return computed();
        }
    },
    template: function (template, params) {
        var templateHtml = $('#' + template).html();
        if (!templateHtml) {
            throw "Error template " + template + " was not found.";
        }
        return $.fn.dataTable.render.ko.templateInline(templateHtml, params);
    },
    templateInline: function (template) {
        return function (data, type, full, meta) {
            if (type == 'display') {
                return template;
            }
            return null;
        }
    },
    /**
     * Applies knockout bindings to a data table cell.  This should be passed to the created cell function.  This will
     * apply bindings using the row data in the property data along with the data passed into this function.  Note:
     * calling invalidate on a cell will break this if you invalidate a cell you should clean the cell using
     * ko.cleanNode then reapply the bindngs.
     * @param data - additional data to apply bindings with.  This will merge data with the rowData
     * @returns {Function}
     */
    applyBindings: function(data) {
        return function(td, cellData, rowData, row, col) {
            ko.applyBindings($.extend({
                data: rowData
            }, data), td);
        }
    },
    truncated: function (length) {
        return function (data, type, full, meta) {
            var wordBreak = true;
            var text = $.fn.dataTable.render.ko.observable()(data, type, full, meta);
            return oeca.common.utils.truncateSpan(text);
        };
    },
    action: function (name, action, cssClass, table) {
        $(table).on('click', '[data-ko-action-name="' + name + '"]', function (event) {
            var row = $(event.target).closest('tr');
            var dtRow = $(table).DataTable().row(row);
            var data = dtRow.data();
            action(data, row);
        });
        $(table).on("destroy.dt", function () {
            $(table).off();
        });
        return function (data, type, full, meta) {
            btnClass = cssClass || 'btn-primary';
            return '<button class="btn ' + btnClass + '" data-ko-action-name="' + name + '">' + name + '</button>';
        }
    },
    actions: function (actions, table) {
        var html = '';
        for (var i = 0; i < actions.length; ++i) {
            var action = actions[i];
            html += $.fn.dataTable.render.ko.action(action.name, action.action, action.cssClass, table)();
        }
        return function (data, type, full, meta) {
            return html;
        }
    }
};

/*
 * Sets up some javascript events to work around a bug in firefox where the css
 * selector '.file-input:focus + label' does not work.
 */
ko.bindingHandlers.fileUploadButton = {
    init: function (element, valueAccessor, allBindings) {
        $(element).focus(function () {
            $(element).siblings('label').addClass('file-input-focused');
        })
        $(element).blur(function () {
            $(element).siblings('label').removeClass('file-input-focused');
        });
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            $(element).off();
        });
    }
}

ko.bindingHandlers.leaflet = {
    init: function (element, valueAccessor, allBindings) {
        var options = valueAccessor();
        //TODO dispose?
        var noWrapOption = {
            noWrap: true
        };
        var map = L.map(element);
        L.esri.basemapLayer('Imagery', noWrapOption).addTo(map);
        L.esri.basemapLayer('ImageryLabels', noWrapOption).addTo(map);
        if (options.latLongMarker) {
            var latLongObj = options.latLongMarker.latLong;
            var marker;

            function setMarker(latlng) {
                //validate lat and long are present
                if (latlng) {
                    if (!marker) {
                        marker = L.marker(latlng).addTo(map);
                    }
                    else {
                        marker.setLatLng(latlng);
                    }
                }
                else {
                    //if there is a marker remove it.
                    map.removeLayer(marker);
                    marker = null;
                }
            }

            if (latLongObj.getLatLong) {
                if (latLongObj.getLatLong()) {
                    setMarker(latLongObj.getLatLong());
                }
                latLongObj.getLatLong.subscribe(function () {
                    setMarker(latLongObj.getLatLong());
                });
            }
            function onMapClick(e) {
                setMarker(e.latlng);
                latLongObj.setLatLong(e.latlng);
            }

            map.on('click', onMapClick);
        }
        if (options.center) {
            if (options.center.lat && options.center.long) {
                map.setView([options.center.lat, options.center.long], 10);
            }
            else if (options.center.city && options.center.state) {
                oeca.common.map.centerCityState(map, options.center.city, options.center.state);
            }
            else if (options.center.state) {
                oeca.common.map.center(map, oeca.common.map.decodeState(options.center.state));
            }
            else {
                oeca.common.map.center(map, "US");
            }
        }
        else {
            oeca.common.map.center(map, "US");
        }
    }
}
ko.bindingHandlers.maskedLatLong = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask || "[-]999[.999]";
        //TODO add disposal logic?
        $(element).inputmask('numeric', {
            digits: 4,
            integerDigits: 3,
            digitsOptional: true,
            placeholder: '',
            showMaskOnHover: true,
            rightAlign: false,
            showMaskOnFocus: true,
            allowMinus: true
        });
        ko.bindingHandlers.value.init(element, valueAccessor, allBindingsAccessor);
    },
    update: function (element, valueAccessor) {
        ko.bindingHandlers.maskedInput.update(element, valueAccessor);
    }
};
ko.bindingHandlers.maskedDischargeId = {
    init: function (element, valueAccessor, allBindings, viewModel) {
        $(element).inputmask('numeric', {
            digits: 0,
            integerDigits: 3,
            min: 1,
            max: 999,
            mask: '999',
            numericInput: true,
            placeholder: '000',
            showMaskOnHover: true,
            rightAlign: false,
            showMaskOnFocus: true,
            allowMinus: false
        });
        ko.bindingHandlers.value.init(element, valueAccessor, allBindings, viewModel);
    },
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        value($(element).val());
        value();
    }
};
ko.bindingHandlers.maskedNumberWithCommas = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask;
        $(element).inputmask('numeric', {
            groupSeparator: ',',
            autoGroup: true,
            allowMinus: false,
            allowPlus: false,
            rightAlign: false
        });
        ko.bindingHandlers.value.init(element, valueAccessor, allBindingsAccessor);
    },
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        var number = ko.utils.unwrapObservable(value);
        if (number != null) {
            number = number.toString().replace(',', '');
            value(number);
        }
    }
};
ko.bindingHandlers.maskedPercentage = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask;
        $(element).inputmask('numeric', {
            allowMinus: false,
            allowPlus: false,
            rightAlign: false,
            suffix: '%'
        });
        ko.bindingHandlers.value.init(element, valueAccessor, allBindingsAccessor);
    },
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        var number = ko.utils.unwrapObservable(value);
        if (number != null) {
            number = number.toString().replace('%', '');
            value(number);
        }
    }
};

ko.bindingHandlers.cromerr = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        var params = valueAccessor();
        var callback;
        var validate;
        var disclaimerTitle;
        var disclaimerText;
        if (typeof params == "function") {
            callback = params;
        }
        else if (typeof params == "object") {
            callback = params.callback;
            validate = params.validate;
            disclaimerTitle = params.disclaimerTitle;
            disclaimerText = params.disclaimerText;
        }
        else {
            throw "Value for cromerr binding must be an object or function";
        }
        var transactionId = oeca.cromerr.createTransaction(callback);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            oeca.cromerr.disposeTransaction(transactionId);
        });
        return ko.bindingHandlers.click.init(element, function () {
            return function () {
                if (validate && !validate()) {
                    //failed validation don't start widget yet.
                    return;
                }
                if (disclaimerTitle || disclaimerText) {
                    oeca.common.notifications.cromerrDisclaimer(disclaimerTitle, disclaimerText, null, function() {
                        oeca.cromerr.startTransaction(transactionId);
                    });
                }
                else {
                    oeca.cromerr.startTransaction(transactionId);
                }
            }
        }, allBindingsAccessor, viewModel, bindingContext)
    }
}

ko.observable.fn.withPausing = function() {
  this.notifySubscribers = function() {
        if (!this.pauseNotifications) {
            ko.subscribable.fn.notifySubscribers.apply(this, arguments);
        }
    };

    this.silentUpdate = function(newValue) {
        var self = this;
        self.pauseNotifications = true;
        self(newValue);
        self.pauseNotifications = false;
    };

    return this;
};

ko.subscribable.fn.subscribeChanged = function (callback) {
    var oldValue;
    this.subscribe(function (_oldValue) {
        oldValue = _oldValue;
    }, this, 'beforeChange');

    this.subscribe(function (newValue) {
        callback(newValue, oldValue);
    });
};

//lookup code
var lookups = {};
var LookupViewModel = function (settings) {
    var self = this;
    self.value = settings.lookupArray;
    self.loadDeferred = null;
    self.load = settings.load || function () {
            if (!self.loadDeferred) {
                self.loadDeferred = $.Deferred();
                $.getJSON(settings.url, function (data) {
                    var options = settings.viewModel ? {
                            '': {
                                create: function (options) {
                                    return new settings.viewModel(options.data);
                                }
                            }
                        } : {};
                    ko.mapping.fromJS(data, options, self.value);
                    self.loadDeferred.resolve();
                });
            }
            return self.loadDeferred;
        }
}
function registerLookupWithSettings(name, settings) {
    if (lookups[name]) {
        throw name + " is already a registered lookup";
    }
    if (lookups[name + 'VM']) {
        throw name
        + "VM is already registed this name must be free to register a lookup.  This name holds the control for the lookup.";
    }
    lookups[name] = ko.observableArray([]);
    settings = $.extend({
        lookupArray: lookups[name],
        name: name
    }, settings);
    lookups[name + 'VM'] = new LookupViewModel(settings);
}
function registerLookup(name, url, viewModel) {
    registerLookupWithSettings(name, {
        url: url,
        viewModel: viewModel
    });
}
function registerLookupCustomLoad(name, loadFunc) {
    registerLookupWithSettings(name, {
        load: loadFunc
    });
}
function loadLookup(name) {
    var vm = lookups[name + 'VM'];
    if (vm) {
        return vm.load();
    } else {
        throw name + ' is not a registered lookup';
    }
}

//utility functions
function parseProperty(object, prop) {
    var props = prop.split('.');
    var target = object;
    for(var i = 0; i < props.length; ++i) {
        if(!target) {
            return target;
        }
        target = ko.utils.unwrapObservable(target[props[i]]);
    }
    return target;
}

//knockout array functions
ko.observableArray.fn.lookup = function(value, compareFunc) {
    return ko.utils.arrayFirst(this(), function(item) {
        return compareFunc(item, value);
    });
}

ko.observableArray.fn.lookupByProp = function(value, property) {
    return this.lookup(value, function(item, value) {
        return parseProperty(item, property) == ko.utils
                .unwrapObservable(value);
    });
}

ko.observableArray.fn.lookupById = function(value) {
    return this.lookup(value, function(item, value) {
        return ko.utils.unwrapObservable(item.id) == ko.utils
                .unwrapObservable(value);
    });
}

ko.observableArray.fn.filter = function(value, compareFunc) {
    return ko.utils.arrayFilter(this(), function(item) {
        return compareFunc(item, value);
    });
}

ko.observableArray.fn.filterByProp = function(value, property) {
    return this.filter(value, function(item, value) {
        return ko.utils.unwrapObservable(parseProperty(item, property)) == ko.utils.unwrapObservable(value);
    })
}

//bindings
ko.bindingHandlers.select2 = {
    after: ['options', 'lookup', 'value', 'lookupValue', 'selectedOptions'],
    init: function (element, valueAccessor, allBindings) {
        var options = ko.toJS(valueAccessor()) || {};
        var defaultOptions = {};
        if($(element).parents('.modal-dialog').length > 0) {
            //fix for when a select2 is in a bootstrap modal
            //https://github.com/select2/select2-bootstrap-theme/issues/41
            defaultOptions.dropdownParent = $(element).parents('.modal-dialog')
        }
        options = $.extend(defaultOptions, options);
        var subscriptions = [];
        // timeout fixes a problem with select2 not showing the intial value.
        setTimeout(function () {
            $(element).select2(options);
            var subscribeToBinding = function (binding) {
                if (allBindings.has(binding) && allBindings()[binding]) {
                    subscriptions.push(allBindings()[binding].subscribe(function (newVal) {
                        setTimeout(function () {
                            // use another thread so all other subscriptions
                            // finish first
                            $(element).trigger("change.select2");
                        }, 0);
                    }));
                }
            }
            subscribeToBinding('options');
            subscribeToBinding('value');
            subscribeToBinding('lookupValue');
            subscribeToBinding('selectedOptions');
            //Workaround to fix tabbing resetting after using select2 https://github.com/select2/select2/issues/4384
            $(element).on('select2:close', function() {
                $(this).focus();
            });
        }, 0);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            for (var i = 0; i < subscriptions.length; ++i) {
                subscriptions[i].dispose();
            }
            if ($(element).data('select2'))
            {
                $(element).select2('destroy');
            }
            $(element).off();
        });
    },
    update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var observable = allBindingsAccessor().value || {};
        if (typeof allBindingsAccessor().selectedOptions == "function") {
            observable = allBindingsAccessor().selectedOptions;
        }
        var value = ko.utils.unwrapObservable(observable);
        $(element).trigger("change.select2");
    }
};
/**
 * When using select2's ajax functionality it will only assign the value of the select to a primitive.  Instead of the
 * value binding you may use this binding which will pull the full object from select2 and set it as your value.
 * Usually the value comes back as a generic js object and will not have its property set up as observables if you want
 * the properties to also be observable you should provide a model object.
 *
 * The value you provide must be an observable.  You may provide the value either as the value of the binding or in
 * object as the value property
 *
 * Example: as value
 * <select data-bind="select2:{}, select2AjaxValue: myObservable"></select>
 *
 * As an object
 * <select data-bind="select2:{},
 *                    select2AjaxValue: {
 *                        value: myObservable,
 *                        model: myModel
 *                    }"></select>
 *
 * Multi Select Support
 * If you select is set to multiple your observable must be an observable array.  You will also need to provide a
 * compare function.  When the user removes a value from select2 the binding will loop through the selected options and
 * call your compare function passing in the value to be removed as the first arg and the current array item as the
 * second arg.  If you return true this binding will remove that value from the selected options.  Note: This will only
 * remove from the selected options if you return false always in your compare function the value will be removed from
 * select2 but not your selected options.  Alternatively if you always true all your values in your selected options
 * will be removed but select2 will still display them as selected.  Since user's can only remove items one at time
 * this will stop removing items after the first result returned is true.
 *
 * Example:
 * <select data-bind="select2:{},
 *                    select2AjaxValue: {
 *                        value: myObservableArray,
 *                        model: myModel,
 *                        compare: function(toRemove, item) {
                                return toRemove.id == item.code();
                            }
 *                    }" multiple></select>
 *
 * Make sure you do not use the value binding with this binding as the value binding will overwrite this one with the
 * wrong value.  It will end up being a primitive instead of the object.  If you only need a primitive then don't bother
 * with this binding and use the normal value binding.
 *
 * Note: when using this binding if you reload the page this value will be set to your observable but not in the select2
 * list.  Since your value is a complex object there is no way to set it as selected in select2.  So to display the
 * value as selected in select2 you should edit the placeholder to show your selected value.
 * For example:
 * <select data-bind="select2:{
 *                      placeholder: ko.pureComputed(function() {
 *                          if(myObservable() && myObservable().name()) {
 *                              return myObservable().name();
 *                          }
 *                          return 'Select a value';
 *                      })
 *                  }, select2AjaxValue: myObservable"></select>
 *
 */
ko.bindingHandlers.select2AjaxValue = {
    init: function (element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var bindingValue = valueAccessor();
        var value = bindingValue.value || bindingValue;
        var model = bindingValue.model;
        var compare = bindingValue.compare;
        var selectedPlaceHolder = bindingValue.selectedPlaceHolder;
        var multi = $(element).prop('multiple');
        if (!ko.isWriteableObservable(value)) {
            console.log(value);
            console.log(element);
            throw "select2AjaxValue value option must be a writeable observable";
        }
        if(multi && !value.push) {
            throw "Select is a multi select koValue must be an observable array";
        }
        if(multi && (!compare || typeof compare != "function")) {
            throw "Select is a multi select you must provide a compare function otherwise the select will not be able to remove values."
        }
        $(element).on('select2:select', function (evt) {
            var selectedValue = model ? new model(evt.params.data) : evt.params.data;
            if(multi) {
                value.push(selectedValue);
            }
            else {
                value(selectedValue);
            }
            return true;
        });
        $(element).on('select2:unselect', function(evt) {
            if(multi) {
                var itemsToRemove = [];
                ko.utils.arrayForEach(value(), function(item) {
                    if(compare(evt.params.data, item)) {
                        itemsToRemove.push(item);
                    }
                });
                if(itemsToRemove.length > 0) {
                    ko.utils.arrayForEach(itemsToRemove, function(item) {
                        value.remove(item);
                    })
                }
            }
            else {
                value(null);
            }
            return true;
        });
        //make this binding validatable.  We can't use the normal function because the observable value is wrapped in
        // an object.
        return ko.bindingHandlers['validationCore'].init(element, function() {
            return value;
        }, allBindings, viewModel, bindingContext);
    }
}

function extractOptions(value) {
    var options = value.options || value;
    if (typeof options === 'string') {
        if (lookups[options].length == 0) {
            loadLookup(options);
        }
        options = lookups[options];
    }
    var optionsComputed;
    if (value.filter) {
        if (!(value.filter.value && value.filter.by)) {
            throw "The value and by parameters are required to use the filter.  value: " + value.filter.prop + ", by: " + value.filter.value;
        }
        if (typeof value.filter.value == "function" && !ko.isObservable(value.filter.value)) {
            optionsComputed = ko.pureComputed(function () {
                return options.filter(value.filter.by, value.filter.value);
            });
        }
        else {
            optionsComputed = ko.pureComputed(function () {
                return options.filterByProp(value.filter.by, value.filter.value);
            });
        }
    }
    return optionsComputed || options;
}
/**
 * This binding is a wrapper for the options binding. If you provide a string
 * this will look for the options in the global lookup variable. Otherwise this
 * works exactly like the options binding.
 */
ko.bindingHandlers.lookup = {
    init: function (element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var value = valueAccessor();
        var options = extractOptions(value);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            if (options.dispose) {
                options.dispose();
            }
        });
        return ko.bindingHandlers.options.init(element, function () {
            return options
        }, allBindings, viewModel, bindingContext);
    },
    update: function (element, valueAccessor, allBindings, viewModel,
                      bindingContext) {
        var value = valueAccessor();
        var options = extractOptions(value);
        var result = ko.bindingHandlers.options.update(element, function () {
            return options
        }, allBindings, viewModel, bindingContext);
        $(element).trigger('change.select2');
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            if (options.dispose) {
                options.dispose();
            }
        });
        return result;
    }
}
function retrieveOptionsFromBindings(allBindings) {
    if (allBindings().options) {
        return allBindings().options;
    }
    else {
        return extractOptions(allBindings().lookup);
    }
}
ko.bindingHandlers.lookupValue = {
    init: function (element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var value = valueAccessor();
        var unwrappedValue = ko.utils.unwrapObservable(value);
        //stuff to clean up
        var toDispose = [];
        // We will need a value binding on the element so see if there is one if
        // not create it.
        var selectValue;
        // make sure we won't get any errors before trying to initialize it so
        // we can provide a better message to the user.
        if (allBindings.has('optionsValue')) {
            // TODO initialize this
            selectValue = ko.observable();
        } else {// friendly error the user so they can fix the problem
            throw "When using the lookupValue binding you must provide the 'optionsValue' binding";
        }
        // get the lookup array for this select from the options binding and set
        // up a function so we can find values in the lookup.
        var selectArray = retrieveOptionsFromBindings(allBindings);
        var comparisionFunc = function (item, value) {
            return ko.utils.unwrapObservable(item[allBindings().optionsValue]) == ko.utils
                    .unwrapObservable(value);
        };
        // see if we have a property already if so we will need to update the
        // value attribute so the select list gets updated correctly.
        if (unwrappedValue
            && unwrappedValue[allBindings().optionsValue]
            && unwrappedValue[allBindings().optionsValue]()) {
            selectValue(unwrappedValue[allBindings().optionsValue]());
        }
        // the select value may also be defaulted if so update the lookupValue
        else if (ko.utils.unwrapObservable(selectValue)) {
            var val = ko.utils.arrayFirst(selectArray(), function (item) {
                return comparisionFunc(item, ko.utils
                    .unwrapObservable(selectValue));
            });
            value(val);
        }
        // set up the subscribe event so we can keep the main object updated
        // when the select changes
        var selectValSubscription = selectValue.subscribe(function (newVal) {
            if (newVal) {
                /*
                 * TODO we should only have one computed observable per a
                 * filtered array. For some reason storing this in select array
                 * does not trigger notifications on change even though the ui
                 * updates correctly when the array filter changes the select
                 * stops working. The select value updates properly but then
                 * this bit of code runs and it is searching for the id of the
                 * selected option in the select array which is not updated
                 * correctly. Need to figure out why it won't update properly.
                 * Maybe we should use the raw options not the filtered array?
                 * Note: this may be creating multiple filtered arrays and may
                 * have memory leaks
                 */
                var val = ko.utils.arrayFirst(retrieveOptionsFromBindings(allBindings)(), function (item) {
                    return comparisionFunc(item, ko.utils
                        .unwrapObservable(newVal));
                });
                value(val);
            }
        });
        toDispose.push(selectValSubscription);
        // if the lookupValue property changes we need to copy the property to
        // the select value so the drop down updates
        if (!(value)) {
            throw "The value your provided to the lookupValue binding is "
            + value
            + ".  This must be defined for the lookupValue to work.";
        }
        if (!value.subscribe) {
            throw "The value you provided must be an observable for the lookupValue binding to work.";
        }
        var valSubscription = value.subscribe(function (newVal) {
            if (newVal) {
                selectValue(ko.utils
                    .unwrapObservable(newVal[allBindings().optionsValue]));
            } else {
                selectValue(undefined);
            }
        });
        toDispose.push(valSubscription);
        // if the options change we need to look up the id again
        var options = retrieveOptionsFromBindings(allBindings);
        if (!(options)) {
            throw "The options provided in your select is "
            + allBindings().options
            + ".  This must be defined for the lookupValue to work.  Did you forget to add the options or lookup binding to your select?  If you are mapping it asynchronously try setting it to ko.observableArray([])";
        }
        var optionsSubscription = options.subscribe(function (newOpts) {
            var val = ko.utils.arrayFirst(selectArray(), function (item) {
                return comparisionFunc(item, ko.utils
                    .unwrapObservable(selectValue));
            });
            value(val);
        });
        toDispose.push(optionsSubscription);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            for (var i = 0; i < options.length; ++i) {
                toDispose[i].dispose();
            }
        });
        // bind the temporary variable to the value binding so it gets updated.
        return ko.bindingHandlers.value.init(element, function () {
            return selectValue
        }, allBindings, viewModel, bindingContext);
    }
}

//radio button lookup
ko.bindingHandlers.foreachLookup = {
    init : function(element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var value = valueAccessor();
        var options = extractOptions(value);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
            if(options.dispose) {
                options.dispose();
            }
        });
        return ko.bindingHandlers.foreach.init(element, function() {
            return options
        }, allBindings, viewModel, bindingContext);
    },
    update : function(element, valueAccessor, allBindings, viewModel,
                      bindingContext) {
        var value = valueAccessor();
        var options = extractOptions(value);
        var result = ko.bindingHandlers.foreach.update(element, function() {
            return options
        }, allBindings, viewModel, bindingContext);
        //$(element).trigger('change.select2');
        ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
            if(options.dispose) {
                options.dispose();
            }
        });
        //return result;
        return { controlsDescendantBindings: true };
    }
}
ko.bindingHandlers.BSModal = {
    init : function(element, valueAccessor) {
        var value = valueAccessor();
        $(element).modal({
            keyboard : true,
            show : ko.unwrap(value),
            backdrop: 'static'
        });
    },
    update : function(element, valueAccessor) {
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).modal('show') : $(element).modal('hide');
    }
};
ko.bindingHandlers.collapse = {
    update: function(element, valueAccessor) {
        var value = valueAccessor();
        ko.utils.unwrapObservable(value) ? $(element).collapse('show') : $(element).collapse('hide');
    }
}
ko.bindingHandlers.openModal = {
    init: function(element, valueAccessor, allbindings, viewModel, bindingContext) {
        var settings = valueAccessor();
        return ko.bindingHandlers.click.init(element, function() {
            return function() {
                openModal(settings.name, settings.id, settings.params);
            }
        }, allbindings, viewModel, bindingContext)
    }
}
ko.bindingHandlers.close = {
    init: function(element, valueAccessor, allbindings, viewModel, bindingContext) {
        return ko.bindingHandlers.click.init(element, function() {
            return function() {
                bindingContext.$data.closeModal(valueAccessor());
            }
        }, allbindings, viewModel, bindingContext)
    }
}
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
ko.bindingHandlers.slideVisible = {
    init: function(element, valueAccessor) {
        // Initially set the element to be instantly visible/hidden depending on the value
        var value = valueAccessor();
        $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
    },
    update: function(element, valueAccessor) {
        // Whenever the value subsequently changes, slowly fade the element in or out
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).slideDown() : $(element).slideUp();
    }
};
ko.bindingHandlers.scrollTo = {
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        if (ko.utils.unwrapObservable(value)) {
            if ($(element).is(':visible')) {
                oeca.utils.scrollToElement(element);
            }
        }
    }
};
ko.bindingHandlers.enableAll = {
    update: function(elem, valueAccessor) {
        var enabled = ko.utils.unwrapObservable(valueAccessor());

        ko.utils.arrayForEach(elem.getElementsByTagName('input'), function(i) {
            i.disabled = !enabled;
        });
    }
};
ko.bindingHandlers.disableSection = {
    update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var value = valueAccessor();
        $(element).find(':input').prop('disabled', ko.utils.unwrapObservable(value));
        //special case to disable the file input widget button
        var fileInputButtons = $(element).find('.file-input-component label.btn');
        if (ko.utils.unwrapObservable(value)) {
            fileInputButtons.addClass('disabled');
        }
        else {
            fileInputButtons.removeClass('disabled');
        }
    }
};

ko.bindingHandlers.iframeContent = {
    update: function(element, valueAccessor) {
        var value = ko.unwrap(valueAccessor());
        element.contentWindow.document.close(); // Clear the content
        element.contentWindow.document.write(value);
    }
};

ko.bindingHandlers.maskedDate = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask || "99/99/9999";
        ko.bindingHandlers.maskedInput.init(element, valueAccessor, allBindingsAccessor, viewModel, mask);
    },
    update: function (element, valueAccessor) {
        ko.bindingHandlers.maskedInput.update(element, valueAccessor);
    }
};

/**
 * This binding is a utility function for components.  A user can pass dom nodes to the component in its body and those
 * nodes can be referenced by the component.  This will loop through those components and look for an node with the id
 * passed in.  If an id is passed in all nodes in this element will be removed and replaced with that node.  This is
 * useful to providing a default bit of html and then allowing the user to override that html.
 *
 * Example:
 * component
 * <!-- ko templateNodeId: 'title' -->
 *     <h1>My Title<h1>
 * <!-- /ko -->
 * component tag
 * <my-component>
 *     <h2 id="title">A Different Title</h2>
 * </my-component>
 * In this case "<h1>My Title<h1>" would be replaced by "<h2 id="title">A Different Title</h2>
 *
 * example2:
 * <my-component></my-component>
 * In this case no node with id "title" was found so "<h1>My Title</h1>" remains in the html.
 *
 * @see http://knockoutjs.com/documentation/component-custom-elements.html#passing-markup-into-components
 * @type {{init: ko.bindingHandlers.templateNodeId.init}}
 */
ko.bindingHandlers['templateNodeId'] = {
    init: function(elem, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        var id = ko.utils.unwrapObservable(valueAccessor());
        if(bindingContext.$componentTemplateNodes) {
            for(var i in bindingContext.$componentTemplateNodes) {
                var node = bindingContext.$componentTemplateNodes[i];
                if(node.id === id) {
                    ko.virtualElements.setDomNodeChildren(elem, [node]);
                    return;
                }
            }
        }
    }
}
ko.virtualElements.allowedBindings['templateNodeId'] = true;
ko.bindingHandlers['loading'] = {
    update: function(elem, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        var val = valueAccessor();
        if(ko.utils.unwrapObservable(val)) {
            $(elem).button('loading');
        }
        else {
            $(elem).button('reset');
        }
    }
}
// EnterKey binding
ko.bindingHandlers.enterKey = {
	init: function (element, valueAccessor, allBindings, data, context) {
		var wrapper = function (data, event) {
			if (event.keyCode === 13) {
				valueAccessor().call(this, data, event);
			}
		};
		ko.applyBindingsToNode(element, { event: { keyup: wrapper } }, context);
	}
};