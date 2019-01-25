/**
 * depends on operator-model.js
 */
var OperatorInformationController = function(data, params) {
    var self = this;
    ko.utils.extend(self, params.operator());
    self.lockName = params.lockName;
	self.hideCounty = params.hideCounty;
    self.labels = $.extend({
        heading: 'Operator Information',
        operatorHeading: 'Operator Name',
        operatorName: 'Operator Name',
        addressHeading: 'Operator Mailing Address',
        contactHeading: 'Operator Point of Contact Information'
    }, params.titles);
    /*
	 * validations
	 */
	self.operatorName.extend({required: true, maxLength: 80});
	self.operatorAddress.address1.extend({required: true, maxLength: 50});
	self.operatorAddress.city.extend({required: true, maxLength: 30});
	self.operatorAddress.stateCode.extend({required: true});
	self.operatorAddress.zipCode.extend({required: true, zipCodeUS: true});
	if (!self.hideCounty) {
		self.counties = ko.computed(function() {
			return lookups.counties.filterByProp(self.operatorAddress.stateCode(), 'stateCode');
		});
		self.operatorAddress.county.extend({required: {
			onlyIf: function() {
				return self.counties().length > 0;
			}
		}});
	}
	self.operatorPointOfContact.firstName.extend({required: true, maxLength: 30});
	self.operatorPointOfContact.lastName.extend({required: true, maxLength: 30});
	self.operatorPointOfContact.phone.extend({required: true, phoneUS: true});
	self.operatorPointOfContact.phoneExtension.extend({maxLength: 4, phoneExtensionUS: true});
	self.operatorPointOfContact.email.extend({required: true, maxLength: 100, email: true});
	self.operatorPointOfContact.title.extend({required: true, maxLength: 40});
	self.errors = ko.validation.group(self, {deep: true});
	self.componentLoaded = function() {
			params.onLoad(true);
    };
    self.dispose = function() {
        oeca.common.utils.dispose(self.counties);
    }
};

var OperatorInformationViewController = function(data, params) {
    var self = this;
    ko.utils.extend(self, params.operator());
    self.lockName = params.lockName;
	self.hideCounty = params.hideCounty;
    self.labels = $.extend({
        heading: 'Operator Information',
        operatorHeading: 'Operator Name',
        operatorName: 'Operator Name:',
        addressHeading: 'Operator Mailing Address',
        contactHeading: 'Operator Point of Contact Information'
    }, params.titles);
};

ko.components.register('operator', {
	viewModel: {
		viewModelClass: OperatorInformationController
	},
	template: {
		component: 'operator'
	}
});

ko.components.register('operator-view', {
	viewModel: {
		viewModelClass: OperatorInformationViewController
	},
	template: {
		component: 'operator-view'
	}
});