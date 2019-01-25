/**
 * Created by smckay on 7/6/2017.
 */
var Attachment = function(data) {
    var self = this;
    data = $.extend({
        name: null,
        createdDate: null,
        category: "Default",
        size: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
    self.errorMessage = ko.observable(null);
    self.formData = function() {
        var formData = new FormData();
        formData.append("file", self.file());
        formData.append("meta", ko.mapping.toJSON(self, {
            ignore: ["createdDate", "file", "lastModified", "lastModifiedDate", "type", "webkitRelativePath", "status"]
        }));
        return formData;
    }
    self.sizeDisplay = ko.pureComputed(function() {
        if(self.size()) {
            if(self.size() > 1<<20) {
                return (self.size()/(1<<20)).toFixed(2) + ' MB';
            }
            else if(self.size() > 1<<10) {
                return (self.size()/(1<<10)).toFixed(2) + ' KB';
            }
            return self.size() + ' B';
        }
    });
}