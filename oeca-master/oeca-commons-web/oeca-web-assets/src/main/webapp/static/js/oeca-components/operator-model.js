/**
 * depends on contact-model.js, address-model.js
 */
var OperatorInformation = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		operatorAddress: {
			create: function(options) {
				return new Address(options.data);
			}
		},
		operatorPointOfContact: {
			create: function(options) {
				if(!options.data) {
					options.data = {
						title: null
					}
				}
				return new Contact(options.data);
			}
		},
		preparer: {
			create: function(options) {
				if(options.data) {
					return ko.observable(new Contact(options.data));
				}
				else {
					return ko.observable(null);
				}
			}
		},
		certifier: {
			create: function(options) {
                if(options.data) {
                    return ko.observable(new Contact(options.data));
                }
                else {
                    return ko.observable(null);
                }
            }
		}
	}, self);
}