/**
 * Created by smckay on 7/6/2017.
 */
var FileUploadController = function(data, params) {
    var self = this;
    self.id = params.id;
    self.attachments = params.attachments;
    self.uploader = ko.observable(new FileUploader(params.apiMethod(), params.category));
};
var FileUploader = function(apiMethod, category) {
    var self = this;
    self.fileArray = ko.observableArray([]);
    self.subscriptions = [];
    var postalSubs = [];
    self.subscriptions.push(self.fileArray.subscribe(function() {
        if(self.fileArray().length == 0) {
            return;
        }
        var formData = new FormData();
        var fileAdded = false;
        formData.append("meta", ko.toJSON({
            category: category
        }));
        var files = [];
        for(var i = 0; i < self.fileArray().length; ++i) {
            var file = self.fileArray()[i];
            var meta = new Attachment(self.fileArray()[i]);
            meta.category(category);
            if(!meta.status) {
                meta.status = ko.observable('pending');
            }
            else {
                meta.status('pending');
            }
            //TODO this should prob be called multi file uploader and a single version should be made too.
            self.filesUploading.push(meta);
            if(meta.size() > 3<<20) {
                meta.status('error');
                meta.errorMessage("File is larger than 3MB.");
            }
            else {
                formData.append("file[]", self.fileArray()[i]);
                fileAdded = true;
            }
            files.push(meta);
        }
        self.fileArray.removeAll();
        if(fileAdded) {
            $.ajax({
                url: apiMethod,
                contentType: false,
                processData: false,
                type: 'put',
                data: formData,
                success: function () {
                    oeca.notifications.showSuccessMessage("Successfully uploaded " + files.length + " files");
                    var uploadedFiles = [];
                    for (var i = 0; i < files.length; ++i) {
                        var file = files[i];
                        if(file.status() != 'error') {
                            file.status('uploaded');
                            uploadedFiles.push(file);
                        }
                    }
                    postalSubs.push(postal.channel('noi').publish("attachment." + category + ".add", uploadedFiles));
                    setTimeout(function () {
                        self.filesUploading.removeAll(files);
                        $(".custom-file-input-clear-button").click();
                    }, 5000);
                },
                error: function (res) {
                    var errorMessage = "We encountered an issue saving your attachment.";
                    if(res.responseJSON) {
                        errorMessage = res.responseJSON.message;
                    }
                    for (var i = 0; i < files.length; ++i) {
                        var file = files[i];
                        file.status('error');
                        file.errorMessage(errorMessage);
                    }
                }
            });
        }
    }));
    self.filesUploading = ko.observableArray([]);
    self.locked = ko.pureComputed(function() {
        return self.filesUploading.filterByProp('pending', 'status').length > 0;
    });
    self.dispose = function() {
        oeca.common.utils.disposeList(self.subscriptions);
        for (var i = 0; i < postalSubs.length; ++i) {
            postal.unsubscribe(postalSubs[i]);
        }
    }
}
ko.components.register('file-input', {
    viewModel: {
        viewModelClass: FileUploadController
    },
    template: {
        component: 'file-input'
    }
});