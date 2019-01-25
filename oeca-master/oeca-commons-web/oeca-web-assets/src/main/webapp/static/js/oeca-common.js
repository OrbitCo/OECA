/**
 * OECA Common Functions, used across all permits, like notifications, utils, etc.
 */
oeca.common = {
	constants: {
		latLongDataSource: {
			Map: 'Map',
			GPS: 'GPS'
		},
		cromerrDisclaimerText: 'I certify under penalty of law that this document and all attachments were prepared under my direction or supervision in accordance with a system designed to assure that qualified personnel properly gathered and evaluated the information submitted. Based on my inquiry of the person or persons who manage the system, or those persons directly responsible for gathering the information, the information submitted is, to the best of my knowledge and belief, true, accurate, and complete. I have no personal knowledge that the information submitted is other than true, accurate, and complete. I am aware that there are significant penalties for submitting false information, including the possibility of fine and imprisonment for knowing violations.'
	},
	map: {
		geocode: function(address, callback) {
            L.esri.Geocoding.geocodeService().geocode().region(address).run(callback);
		},
		geocodeCityState: function(city, state, callback) {
			L.esri.Geocoding.geocodeService().geocode().city(city).region(state).run(callback);
		},
		center: function(map, address) {
			oeca.common.map.geocode(address, function(error, response) {
				if(response.results[0]) {
					map.fitBounds(response.results[0].bounds);
                }
			})
		},
		centerCityState: function(map, city, state) {
			if(state) {
				state = oeca.common.map.decodeState(state);
			}
			if(city && state) {
				oeca.common.map.geocodeCityState(city, state, function(error, response) {
					if(error) {
						console.log(error);
					}
					else if(response.results[0]) {
						map.fitBounds(response.results[0].bounds);
					}
					else {
						oeca.common.map.center(map, state);
					}
				});
			}
			else if (state) {
				oeca.common.map.center(map, state);
			}
			else {
				oeca.common.map.center(map, "US");
			}
		},
		/*
		Some states don't geocode correctly this will adjust the state name to improve the geocoding.
		 */
		decodeState: function(state) {
			if(state == 'AS') {
				return "American Samoa"
			}
			if(state == 'GU') {
				return "Hagatna, GU";
			}
			if(state == 'JA') {
				return "Johnston Atoll";
			}
			if(state == 'MW') {
				return 'Midway Island';
			}
			if(state == 'MP') {
				return 'Northern Mariana Islands';
			}
			if(state == 'PR') {
				return 'Puerto Rico';
			}
			return state;
		}
	},
	notifications: {
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
				}],
				id: null
			}, msg);
			var dialog = new BootstrapDialog(msg);
			dialog.realize();
			if(msg.bodyTitle) {
				dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
			}
			if(msg.helpText) {
				dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
			}
			if(msg.id) {
				dialog.setId(msg.id);
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
                bodyTitle: 'Alert!',
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
                bodyTitle: 'Warning!',
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
		},
        operatorNameLocked: function(field, cancelAction, terminateAction) {
		    oeca.common.notifications.errorAlert({
		        bodyTitle: 'Not Allowed',
                message: 'Since this form has already been submitted to EPA, you must submit a new form to change the Operator Name.',
                buttons: [
                    {
                        label: 'Return',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(cancelAction) {
                                cancelAction(dialogRef);
                            }
                        },
                        cssClass: 'btn-danger-outline'
                    }
                ]
            })
        },
		projectStateLocked: function(cancelAction) {
			oeca.common.notifications.errorAlert({
				bodyTitle: 'Not Allowed',
				message: 'The Project/Site Address State must be the same as the State under Permit Information.',
				buttons: [
					{
						label: 'Ok',
						action: function(dialogRef) {
							dialogRef.close();
							if(cancelAction) {
								cancelAction(dialogRef);
							}
						},
						cssClass: 'btn-danger-outline'
					}
				],
				helpText: 'Your Master Permit number was assigned based on the state reported in the permit information section. To change your project/site state you must create a new form.'
			})
		},
		cromerrDisclaimer: function(disclaimerTitle, disclaimerText, cancelAction, okAction) {
			oeca.common.notifications.successAlert({
			    title: '',
				bodyTitle: (disclaimerTitle ? disclaimerTitle : null),
				message: (disclaimerText ? disclaimerText : oeca.common.constants.cromerrDisclaimerText),
				cssClass: "oeca-alert oeca-success-alert",
				onshow: function (dialog) {
				    dialog.getModal().attr('id', 'disclaimer-modal');
				},
				buttons: [
					{
						label: 'Decline',
						cssClass: 'btn-success-outline',
						action: function(dialogRef) {
							dialogRef.close();
							if(cancelAction) {
								cancelAction(dialogRef);
							}
						}
					},
					{
						label: 'Accept',
						cssClass: 'btn-success',
						action: function(dialogRef) {
							dialogRef.close();
							if(okAction) {
								okAction(dialogRef);
							}
						}
					}
				]
			});
		}
	},
	definitions: {
	    federalOperator: {
            options: {
                title: 'Federal Operator',
                placement: 'top',
                content: 'A "Federal Operator" is an entity that meets the definition of “Operator” in this permit and is either any ' +
                'department, agency or instrumentality of the executive, legislative, and judicial branches of the ' +
                'Federal government of the United States, or another entity, such as a private contractor, ' +
                'performing construction activity for any such department, agency, or instrumentality.'
            }
        },
		latLong: {
			options: {
				placement: 'top',
				container: 'body',
				content: 'Use Decimal Degrees to four decimals'
			}
		},
		mapWidget: {
			options: {
				placement: 'top',
				container: 'body',
				content: 'Map widget uses WGS 84'
			}
		}
	},
	nav: {
		setContext: function(text, contextClass) {
			$('#nav-bar-context').html('<p class="navbar-text">' + text + '</p>');
		}
	},
	utils: {
		truncate: function(text, length) {
            text = text.toString();//cast numbers
            if(text.length < length) {
                return text;
            }
            var trimmed = text.substr(0, length-1);
            //break at word
			trimmed = trimmed.replace(/\s([^\s]*)$/, '');
            return trimmed;
		},
		truncateSpan: function(text, length) {
			var truncatedText = oeca.common.utils.truncate(text, length);
            var span = $('<span>')
			if(truncatedText !== text) {
				span.attr('title', text).append(truncatedText + '...')
			}
			else{
            	span.append(text);
			}
            return $('<div>').append(span).html();
		},
		formatDate: function(date) {
			var unwrappedDate = ko.utils.unwrapObservable(date);
            return unwrappedDate ? moment(unwrappedDate).format('MM/DD/YYYY') : '';
		},
		formatDateTime: function(date) {
			var unwrappedDate = ko.utils.unwrapObservable(date);
            return unwrappedDate ? moment(unwrappedDate).format('MM/DD/YYYY h:mm A') : '';
		},
		convertToIso: function(val) {
			return val !== null ? moment(val, "MM-DD-YYYY").toISOString() : '';
		},
		convertToString: function(val) {
			return val !== null && val !== undefined ? encodeURIComponent(val) : "";
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
        /**
		 * This is a helper function to help set up values to deal with free text entries in a radio button or checkbox
		 * list.  If you want to provide the user a list of options but also allow them to provide any answer in a free
		 * text like:
		 * some question...
		 * * Yes
		 * * No
		 * * Other
		 * If the user selects other they would get a free text entry say they entered maybe.  In your model you want
		 * to store the user's answer in a field answer.  you would set it up like this:
		 * viewModel  = {
		 * 	self.answer = ko.observable(null)
		 * }
		 * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'yes'>
		 * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'no'>
		 * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'other'>
		 * <input type="text" data-bind="value: answer">
		 *
		 * Once the user enters something into the free text entry it would uncheck other.  You can get around this
		 * issue by using a dynamic checkedValue that the text field enters when the checked value changes knockout will
		 * change the checked property and keep the other option checked.  So you would set it up like:
		 * viewModel  = {
		 * 	self.answer = ko.observable(null)
		 * 	self.otherAnswerCheckedValue = ko.observable('other');
		 * }
         * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'yes'>
         * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'no'>
         * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: otherAnswerCheckedValue>
         * <input type="text" data-bind="value: otherAnswerCheckedValue">
		 *
		 * This works fine until you have to load the data back from the server the value of answer would be 'maybe'
		 * but otherAnswerCheckedValue would be reset to 'other' so other will not be checked like you would want.
		 * This function will initialize the value of otherCheckedValue so that when the data backs from the server the
		 * other option is selected.
		 *
		 * This function will loop all your possible values defined in valueList and if it finds a value that is not in
		 * valueList it will set that as the default value for your otherValue object.  If value matches an item in
		 * valueList it will set otherValue to defaultOtherValue.
		 *
		 * If you value object is an array it is assumed you have a checkbox list so the user can select multiple things.
		 * In that case this will loop through all checked values and if it finds one that is not in the list of
		 * available options it will set that as the other checked value.  Note: if for some reason there are multiple
		 * values in your selected list that are not in the valueList it will set otherValue to the last item not found
		 * in valueList.
		 *
         * @param value - the value the radio or checkboxes should be writing to
         * @param otherValue - placeholder to hold checkedValue of the other option
         * @param valueList - Map of all possible values for the radio buttons or checkboxes.  Should not include the
		 * 		other option.
         * @param defaultOtherValue - The default value for other if the other is not selected this value will be used.
         */
        setOtherCheckedValue: function(value, otherValue, valueList, defaultOtherValue) {
			var unwrappedValue = ko.utils.unwrapObservable(value);
			if(unwrappedValue) {
                if(unwrappedValue.push) {
                    //this is an array loop through all the values
                    ko.utils.arrayForEach(unwrappedValue, function(arrayValue) {
                        var matched = false;
                    	for(var v in valueList) {
							if (typeof arrayValue === 'string' || arrayValue instanceof String) {
								if (arrayValue.toLowerCase() == v.toLowerCase()) {
									matched = true;
									break;
								}
							} else {
								if (arrayValue == v) {
									matched = true;
									break;
								}
							}
                        }
                        if(!matched) {
                    		//found a value not in the list must be the value for other.
                    		otherValue(arrayValue);
						}
                    });
                }
                else {
                    for (var v in valueList) {
                        if (unwrappedValue.toLowerCase() == v.toLowerCase()) {
                            //checked value is an actual option.
                            otherValue(defaultOtherValue);
                            return;
                        }
                    }
                    otherValue(unwrappedValue);
                }
			}
			else {
				otherValue(defaultOtherValue);
			}
		},
        /**
		 * Left pads the string s to length.  Will return the string if its length is greater than or equal to the
		 * length passed in.
         * @param s
         * @param length
         * @param padChar
         * @returns {*}
         */
        pad: function(s, length, padChar) {
			var paddedString = '';
            for(var i = 0; i < length; ++i) {
                paddedString += padChar;
            }
            return oeca.common.utils.padWithPaddedString;
		},
        /**
		 * Lefts pads string s to length using part of the padded string.  Padded string should be a string with the
		 * same length as length and the characters you want to pad with.   This will slice the padded string and
		 * append as many characters as needed to s.
         * @param s
         * @param length
         * @param paddedString
         * @returns {*}
         */
		padWithPaddedString: function(s, length, paddedString) {
            if(s == null || s == undefined) {
                return paddedString;
            }
            else if(s.length >= length) {
                return s;
            }
            else {
                return paddedString.slice(String(s).length, length) + s
            }
		},
        /**
		 * Parses the prop passed in walking through each level of the object and unwrapping that level to find the
		 * object.  This is a safe way to get the value of a property when you are not sure if one of the fields along
		 * the path actually exists.
		 *
		 * Example:
		 * obj : {
		 * 	prop1: 1,
		 * 	prop2: ko.observable({
		 * 		prop1: ko.observable(1),
		 * 		prop2: ko.observable(null)
		 * 	});
		 * }
		 * Given the above object if you pass in
		 * oeca.common.utils.parseProperty(obj, 'prop1') - returns 1
		 * oeca.common.utils.parseProperty(obj, 'prop2.prop1') - returns 1,
		 * oeca.common.utils.parseProperty(obj, 'prop2.prop2.prop1') - reutrns null (because prop2 is null)
		 *
         * @param object root object to find the property on
         * @param prop a string with a path to the property you want.  Each level should be separated with a '.'
         * @returns {*} null if some propery along the path was null or undefined other wise returns the value of the
		 * property (which could also be null)
         */
        parseProperty: function(object, prop) {
            var props = prop.split('.');
            var target = object;
            for(var i = 0; i < props.length; ++i) {
                target = ko.unwrap(target[props[i]]);
                if(target == null || target == undefined) {
                    return null;
                }
            }
            return target;
        },
		sequence: 1,
		nextSequence: function() {
        	return ++oeca.common.utils.sequence;
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
        			oeca.common.utils.dispose(item);
				});
			}
			catch (e) {
                if(console.log) {
                    console.log("caught error while trying to dispose list");
                    console.log(list);
                    console.log(e);
                }
			}
		}
	}
}
