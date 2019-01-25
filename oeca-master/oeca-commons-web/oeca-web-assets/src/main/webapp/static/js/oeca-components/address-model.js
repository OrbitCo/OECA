/**
 * depends on oeca-common-models.js
 */
var Address = function(data) {
    var self = this;
    data = $.extend({
        address1: null,
        city: null,
        stateCode: null,
        zipCode: null,
        county: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
    self.fullAddress = ko.pureComputed(function() {
        return self.address1() + ' ' + self.city() + ', ' + self.stateCode() + ' ' + self.zipCode();
    })
}