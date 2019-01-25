var Site = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		address: {
			create: function(options) {
				return new Address(options.data);
			}
		},
		siteLocation: {
			create: function(options) {
				return new Location(options.data);
			}
		}
	}, self);
}
