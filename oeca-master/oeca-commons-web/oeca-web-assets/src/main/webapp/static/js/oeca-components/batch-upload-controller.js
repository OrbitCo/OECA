var BatchUploadController = function(data, params) {
    var self = this;
    self.apiMethod = params.apiMethod;
    self.additionalGuidanceUrl = params.additionalGuidanceUrl;
    ko.mapping.fromJS(data, {}, self);
    self.modalControl = params.modalControl;
    self.successCallback = null;
    self.pendingUpload = ko.observable({
        file: ko.observable()
    });
    self.processingUpload = ko.observable(false);
    self.fileSelected = ko.pureComputed(function() {
    	return self.pendingUpload().fileArray().length > 0;
    });
    self.cancel = function() {
        params.modalControl.closeModal('close');
    };    
    self.upload = function() {
    	self.processingUpload(true);
    	var formData = new FormData();
    	formData.append("file[]", self.pendingUpload().file());
        $.ajax({
            url: self.apiMethod(),
            contentType: false,
            processData: false,
            type: 'put',
            data: formData,
            success: function (result) {
            	self.processingUpload(false);
            	params.modalControl.closeModal('close');
        	    openModal('batch-upload-summary', undefined, {
        	        data: {
        	            summary: result,
        	        	successCallback: function() {
        	                params.modalControl.closeModal('close');
        	            }
        	        }
        	    });
            },
            error: function (res) {
            	console.log(res);
            	self.processingUpload(false);
            }
        });
    };
    var subscriptions = [];
    self.dispose = function() {
        oeca.common.utils.disposeList(subscriptions);
    };
    self.refresh = function(data) {
        ko.mapping.fromJS(data, {}, self);
        self.processingUpload(false);
    }
}

ko.components.register('batch-upload', {
    viewModel: {
        viewModelClass: BatchUploadController,
        modal: true
    },
    template: {
        modal: 'batch-upload'
    }
});
