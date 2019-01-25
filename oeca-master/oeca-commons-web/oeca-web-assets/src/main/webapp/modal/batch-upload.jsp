<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<stripes:layout-render name="/layout/modal-layout.jsp" type="info" addClass="modal-md"
                       title="<span class='glyphicon glyphicon-open'></span>">
    <stripes:layout-component name="body">
        <h3>Batch Upload</h3>
        <div data-bind="visible: !processingUpload()">
	        <div class="row">
	        	<div class="col-md-12 form-group text-left">A CSV document is required for the batch upload. Please ensure all required fields are entered into your CSV document in the correct order.</div>
	        </div>
	        <div class="row">
	        	<div class="col-md-12 form-group text-left"><strong>Note:</strong> <a data-bind="attr: { href: additionalGuidanceUrl }">Additional Guidance for Required Fields</a></div>
	        </div>
	        <div data-bind="fileDrag: pendingUpload">
				<input type="file" id="batchUpload" data-bind="fileInput: pendingUpload, customFileInput: {}"  accept=".csv">
			</div>
		</div>                
        <div data-bind="visible: processingUpload()">
	        <div class="row">
				<div class="col-md-12 form-group text-center">Processing...</div>
	        </div>
	        <div class="row">
				<div class="col-md-12 form-group text-center"><span class="fa fa-spinner fa-2x fa-spin"></span></div>
	        </div>	        
	        <div class="row">
				<div class="col-md-12 form-group text-center">Please Wait...</div>
	        </div>
		</div>                
        <div class="row">
            <div class="col-md-12 form-group">
            	<button id="close-action" class="btn btn-default-outline" data-bind="click: cancel">Cancel</button>            
				<button class="btn btn-primary" data-bind="click: upload, enable: fileSelected && (!processingUpload())">Upload</button>
			</div>
        </div>
    </stripes:layout-component>
</stripes:layout-render>