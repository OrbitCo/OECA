var BatchUpload = function(data) {
    var self = this;
    data = $.extend({
        name: null,
        createdDate: null,
        size: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
}