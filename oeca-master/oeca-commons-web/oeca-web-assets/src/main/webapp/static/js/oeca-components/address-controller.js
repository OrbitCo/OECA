var AddressTemplateController = function(data, params) {
    var self = this;
    self.address = new Address(ko.utils.unwrapObservable(params.address));
    self.labels = $.extend({
    	address1: 'Address',
		address2: 'Address Line 2',
		city: 'City',
		state: 'State',
		zip: 'Zip',
		zip4: 'Ext.',
		county: 'County or Similar Division'
    }, params.titles);
    self.lockState = params.lockState;
    self.lockStateUrl = params.lockStateUrl;
    self.hideCounty = params.hideCounty;
};

ko.components.register('address-info', {
    viewModel: {
        viewModelClass: AddressTemplateController
    },
    template: {
        component: 'address'
    }
});
ko.components.register('address-view', {
    viewModel: {
        viewModelClass: AddressTemplateController
    },
    template: {
        component: 'address-view'
    }
});
