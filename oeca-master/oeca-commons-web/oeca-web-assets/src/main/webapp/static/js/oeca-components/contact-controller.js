/**
 * depends on contact-model.js
 */
var ContactTemplateController = function(data, params) {
    var self = this;
    self.contact = new Contact(ko.utils.unwrapObservable(params.contact));
    self.labels = $.extend({
        firstName: 'First Name',
        middleInitial: 'Middle Initial',
        lastName: 'Last Name',
        name: 'Name',
        organization: 'Organization',
        address: 'Address',
        title: 'Title',
        phone: 'Phone',
        phoneExt: 'Ext.',
        email: 'Email',
        verifyEmail: 'Verify Email',
        ein: 'IRS Employer Identification Number (EIN)'
    }, params.titles);
    self.fullPhone = ko.pureComputed(function() {
        var phone = self.contact.phone();
        if(self.contact.phoneExtension()) {
            phone +='x';
            phone += self.contact.phoneExtension();
        }
        return phone;
    });
};

ko.components.register('contact-view', {
    viewModel: {
        viewModelClass: ContactTemplateController
    },
    template: {
        component: 'contact-view'
    }
});

ko.components.register('contact-info', {
    viewModel: {
        viewModelClass: ContactTemplateController
    },
    template: {
        component: 'contact'
    }
});
