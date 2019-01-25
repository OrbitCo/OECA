var LocationTemplateController = function(data, params) {
    var self = this;
    self.location = params.location;
    self.labels = $.extend({
    	latitude: 'Latitude',
		longitude: 'Longitude',
		latLong: 'Latitude/Longitude',
		dataSource: 'Latitude/Longitude Data Source',
		otherDataSource: 'Other Data Source',
		datum: 'Horizontal Reference Datum'
    }, params.titles);
    self.hideDataSource = params.hideDataSource ? params.hideDataSource : ko.observable(false);
    self.hideDatum = params.hideDatum ? params.hideDatum : ko.observable(false);
    self.hideDatumTooltip = params.hideDatumTooltip ? params.hideDatumTooltip : ko.observable(false);
};

ko.components.register('location-view', {
    viewModel: {
        viewModelClass: LocationTemplateController
    },
	template: {
		component: 'location-view'
	}
});

ko.components.register('location', {
    viewModel: {
        viewModelClass: LocationTemplateController
    },
	template: {
		component: 'location'
	}
});
