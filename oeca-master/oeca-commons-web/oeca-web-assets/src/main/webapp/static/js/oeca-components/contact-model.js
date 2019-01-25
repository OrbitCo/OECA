var Contact = function(data) {
    var self = this;
    data = $.extend({
        userId: null,
        firstName: null,
        middleInitial: null,
        lastName: null,
        phone: null,
        phoneExtension: null,
        email: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
    self.name = ko.pureComputed(function() {
        var firstName = self.firstName() || '';
        var lastName = self.lastName() || '';
        var mi = self.middleInitial() || '';
        if(self.firstName() && self.lastName() && self.middleInitial()) {
            mi += '. ';
        }
        return firstName + ' ' + mi + lastName;
    });
}