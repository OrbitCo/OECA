<!-- ko templateNodeId: 'title' -->
<h1>Request Permissions</h1>
<!-- /ko -->
<table class="table table-striped table-condensed dataTable responsive no-wrap" style="width: 100%">
    <thead>
    <tr>
        <th>Role</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody data-bind="foreach: permissions">
    <tr>
        <td>
            <span data-bind="text: oeca.acl.utils.permissionDisplay($data, $parent.entityConfig)"></span>
        </td>
        <td>
            <!-- ko if: available -->
                <!-- ko if: id -->
                    <!-- ko if: requestStatus -->
                        <span>Request Pending</span>
                    <!-- /ko -->
                    <!-- ko ifnot: requestStatus -->
                        <span>Request Approved</span>
                    <!-- /ko -->
                <!-- /ko -->
                <!-- ko ifnot: id -->
                    <span>Available for Request</span>
                <!-- /ko -->
            <!-- /ko -->
            <!-- ko ifnot: available -->
                <span>Unavailable</span>
            <!-- /ko -->
        </td>
        <td>
            <!-- ko if: id -->
                <!-- ko if: requestStatus -->
                    <button class="btn btn-default btn-xs" data-bind="enable: available, click: $parent.cancelRequest">Cancel</button>
                <!-- /ko -->
                <!-- ko ifnot: requestStatus -->
                    <button class="btn btn-default btn-xs" data-bind="enable: available, click: $parent.removeAccess">Remove</button>
                <!-- /ko -->
            <!-- /ko -->
            <!-- ko ifnot: id -->
                <button class="btn btn-primary btn-xs" data-bind="enable: available, click: $parent.requestAccess, loading: submitting">Request</button>
            <!-- /ko -->
        </td>
    </tr>
    </tbody>
</table>
<ul class="list-unstyled" data-bind="foreach: validations">
    <li class="validationMessage" data-bind="text: $data"></li>
</ul>
<!-- ko templateNodeId: 'help-text' --><!-- /ko -->
<!-- ko templateNodeId: 'pre-actions' --><!-- /ko -->
<!-- ko templateNodeId: 'cancel-action' -->
    <button class="btn btn-default">Cancel</button>
<!-- /ko -->
<!-- ko templateNodeId: 'post-actions' --><!-- /ko -->