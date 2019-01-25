var BatchUploadSummary = function(data) {
    var self = this;
    data = $.extend({
        success: null,
        failure: null,
        total: null,
        scheduled: null
    }, data);
    self.errors = ko.observableArray([]);
    ko.mapping.fromJS(data, {}, self);
}