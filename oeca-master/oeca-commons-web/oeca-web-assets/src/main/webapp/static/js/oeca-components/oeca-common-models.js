/**
 * Base models
 */
var BaseLookupModel = function(data) {
    var self = this;
    data = $.extend({
        code: null,
        description: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
    self.display = ko.pureComputed(function() {
        try {
            return self.code() + ' - ' + self.description();
        }
        catch (error) {
            console.log("error computing display for lookup: " + ko.toJSON(self));
            throw error;
        }
    });
}

