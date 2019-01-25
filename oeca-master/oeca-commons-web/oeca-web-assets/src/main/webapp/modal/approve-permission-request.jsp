<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<stripes:layout-render name="/layout/modal-layout.jsp" type="success" addClass="modal-lg" id="approve-permission-request-modal"
                       title="<span class='glyphicon glyphicon-search'></span>">
    <stripes:layout-component name="body">
        <h1>Approve Permission Request?</h1>
        <!-- ko if: request -->
        <p>Are you sure you want to approve <span data-bind="text: request().user.userId"></span> request for the
            following roles for <span data-bind="text: $data.entityName || 'entity ' + request().entityId()"></span></p>
        <!-- ko if: request().permissions -->
        <p data-bind="text: oeca.acl.utils.permissionsDisplayAsString(request().simplePermissions(), entityConfig)"></p>
        <!-- /ko -->
        <!-- ko ifnot: request().permissions -->
        <p data-bind="text: oeca.acl.utils.permissionDisplay(request(), entityConfig)"></p>
        <!-- /ko -->
        <div class="row">
            <div class="col-sm-12 form-group">
                <label class="sr-only" for="approval-comment">Optional Approval Comment</label>
                <textarea class="form-control" id="approval-comment" placeholder="Enter comment... (optional)" data-bind="value: reviewComment"></textarea>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <button class="btn btn-success-outline" data-bind="click: function(){modalControl.closeModal('cancel')}">Cancel</button>
                <button class="btn btn-success" data-bind="click: approve">Approve</button>
            </div>
        </div>
        <!-- /ko -->
    </stripes:layout-component>
</stripes:layout-render>