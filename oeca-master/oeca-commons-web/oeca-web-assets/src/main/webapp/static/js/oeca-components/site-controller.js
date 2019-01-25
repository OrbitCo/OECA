var SiteController = function(data, params) {
	var self = this;
	ko.utils.extend(self, ko.utils.unwrapObservable(params.siteInformation));
	self.expand = params.toggle;
	self.showIndian = params.showIndian;
    self.lockState = params.lockState;
    self.lockStateUrl = params.lockStateUrl;
    self.labels = $.extend({ 
    	siteName: 'Project/Site Name',
    	addressHeading: 'Project/Site Address',
    	locationHeading: 'Latitude and Longitude'
    }, params.titles);
	/*
	 * validations
	 */
	self.siteName.extend({required: true, maxLength: 80});
	self.siteAddress.address1.extend({required: true, maxLength: 50});
	self.siteAddress.city.extend({required: true, maxLength: 60});
	self.siteAddress.stateCode.extend({required: true});
	self.siteAddress.zipCode.extend({required: true, zipCodeUS: true});
	self.counties = ko.pureComputed(function() {
		return lookups.counties.filterByProp(self.siteAddress.stateCode(), 'stateCode');
	});
	self.siteAddress.county.extend({required: {
		onlyIf: function() {
			return self.counties().length > 0;
		}
	}});
	self.siteLocation.latitude.extend({required: true});
	self.siteLocation.latitudeDisplay.extend({
		required: true,
		number: true,
		min: {
			params: -90,
			message: "Latitude must be between 90 (90 N) and -90 (90 S)."
		},
		max: {
			params: 90,
			message: "Latitude must be between 90 (90 N) and -90 (90 S)."
		}
	});
	self.siteLocation.longitude.extend({required: true});
	self.siteLocation.longitudeDisplay.extend({
		required: true,
		number: true,
		min: {
			params: -180,
			message: "Longitude must be between 180 (180 E) and -180 (180 W)."
		},
		max: {
			params: 180,
			message: "Longitude must be between 180 (180 E) and -180 (180 W)."
		}
	});
	self.siteLocation.latLongDataSource.extend({required: true});
	self.siteLocation.horizontalReferenceDatum.extend({required: true});
	self.siteIndianCountry.extend({required: true});
	self.siteIndianCountryTribe.extend({
		required: {
			onlyIf: function() {
				return self.siteIndianCountry();
			}
		}
	});
};

ko.components.register('site', {
	viewModel: {
		viewModelClass: SiteController,
	},
	template: {
		component: 'site'
	}
});
