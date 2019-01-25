var BatchUploadSummaryController = function(data, params) {
    var self = this;
    ko.mapping.fromJS(data, {}, self);
    self.modalControl = params.modalControl;
    self.successCallback = null;
    self.cancel = function() {
        params.modalControl.closeModal('close');
    };    
    self.download = function() {
        var filename = 'processing_summary.txt';
        var link, txt, data;

        /*
        txt = 'Processing Summary:\n';
        txt += '-------------------\n\n';
        txt += 'Draft forms created: ' + self.summary.success() + '\n';
        txt += 'Forms failing validation: ' + self.summary.failure() + '\n\n';
        
        if (self.summary.errors().length > 0) {
            txt += 'Error Details:\n';
            txt += '--------------\n\n';
        	
            ko.utils.arrayForEach(self.summary.errors(), function(error) {
                txt += 'Row ' + error.row() + ', column ' + error.column() + ': ' + error.reason() + '\n';
            });
        }
        */
        	
        txt = 'data:text/plain;charset=utf-8,' + self.summary.processingReport();
        
        data = encodeURI(txt);

        link = document.createElement('a');
        link.setAttribute('href', data);
        link.setAttribute('download', filename);
        link.click();
    };
    var subscriptions = [];
    self.dispose = function() {
        oeca.common.utils.disposeList(subscriptions);
    };
    self.refresh = function(data) {
        ko.mapping.fromJS(data, {}, self);
    }
}

ko.components.register('batch-upload-summary', {
    viewModel: {
        viewModelClass: BatchUploadSummaryController,
        modal: true
    },
    template: {
        modal: 'batch-upload-summary'
    }
});
