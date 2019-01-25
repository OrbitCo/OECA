<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<stripes:layout-render name="/layout/modal-layout.jsp" type="info" addClass="modal-md"
                       title="<span class='glyphicon glyphicon-open'></span>">
    <stripes:layout-component name="body">
        <h3>Batch Upload Summary</h3>
        <!-- ko if: summary.scheduled() -->
        <div class="row">
        	<div class="col-md-12 form-group text-left">You have initiated the upload of <span data-bind="text: summary.total"></span> structures. Once the upload has completed,
        	a notification email will be sent to your inbox with a processing report containing a summary of any errors encountered during the upload.</div>
        </div>
        <div class="row">
        	<div class="col-md-12 form-group text-left"><strong>Note:</strong> Any forms that do not pass validation will be skipped. Forms will become available to view as drafts
        	once they have completed upload.</div>
        </div>
        <!-- /ko -->
        <!-- ko if: !summary.scheduled() -->
        <div class="row">
        	<div class="col-md-12 form-group text-left">You have created <span data-bind="text: summary.success"></span> new Draft forms. A summary notification has been sent to your email.</div>
        </div>
        <!-- ko if: summary.failure() > 0 -->
        <div class="row">
        	<div class="col-md-12 form-group text-left"><strong>Errors:</strong> <span data-bind="text: summary.failure"></span> forms have failed validation and were not created.</div>
        </div>
        <table class="table table-striped table-condensed dataTable responsive no-wrap">
        	<thead>
	        	<tr>
	        		<th>Row</th>
	        		<th>Column</th>
	        		<th>Reason</th>
	        	</tr>
        	</thead>
        	<tbody data-bind="foreach: summary.errors">
        		<!-- ko if: $index() < 10 -->
		        <tr>
		            <td data-bind="text: row"></td>
		            <td data-bind="text: column"></td>
		            <td data-bind="text: reason"></td>
		        </tr>
		        <!-- /ko -->
	    	</tbody>
        </table>
        <!-- /ko -->
        <br/>
        <!-- /ko -->
        <div class="row">
            <div class="col-md-12 form-group">
            	<button id="close-action" class="btn btn-default-outline" data-bind="click: cancel">Close</button>
            	<button class="btn btn-primary" data-bind="click: download, visible: !summary.scheduled()">Download Processing Summary</button>
        	</div>
        </div>
    </stripes:layout-component>
</stripes:layout-render>