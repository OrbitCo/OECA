<h2>Manage <span data-bind="text: entityName ? entityName : entityId"/>
    Permissions for <span data-bind="text: user().name()"/> (<span data-bind="text: user().userId()"/>)</h2>
<p>Enter a comment (optional) and click "Grant", "Approve" or "Revoke" to manage permissions.</p>
<h3>Current Permissions</h3>
<!-- ko if: currentPermissions() !== undefined && currentPermissions().length > 0 -->
<table class="table table-stripes table-condensed dataTable responsive no-wrap">
    <thead>
    <tr>
        <th>Permission</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody data-bind="foreach: currentPermissions">
    <tr class="text-left">
        <td>
            <span data-bind="text: oeca.acl.utils.permissionDisplay($data, $parent.entityConfig)"></span>
        </td>
        <td>
            <button class="btn btn-default btn-xs" data-bind="click: $parent.revoke, disable: $parent.submitting">Revoke</button>
        </td>
    </tr>
    </tbody>
</table>
<!-- /ko -->
<!-- ko if: currentPermissions() == undefined || currentPermissions().length == 0 -->
<p>The selected user has no permissions.</p>
<!-- /ko -->
<!-- ko if: availablePermissions() !== undefined && availablePermissions().length > 0 -->
<h3>Available Permissions</h3>
<table class="table table-stripes table-condensed dataTable responsive no-wrap">
    <thead>
    <tr>
        <th>Permission</th>
        <th>Comment</th>
        <th>Request Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody data-bind="foreach: availablePermissions()">
    <tr class="text-left">
        <td>
            <span data-bind="text: oeca.acl.utils.permissionDisplay($data, $parent.entityConfig)"></span>
        </td>
        <td>
            <input data-bind="value: reviewComment"/>
        </td>
        <td>
            <!-- ko if: id -->
            <span>Request Pending</span>
            <!-- /ko -->
            <!-- ko ifnot: id -->
            <span>N/A</span>
            <!-- /ko -->
        </td>
        <td>
            <!-- ko if: id -->
            <button class="btn btn-primary btn-xs" data-bind="click: $parent.approve, disable: $parent.submitting">Approve</button>
            <button class="btn btn-danger-outline btn-xs" data-bind="click: $parent.reject, disable: $parent.submitting">Reject</button>
            <!-- /ko -->
            <!-- ko ifnot: id -->
            <button class="btn btn-primary btn-xs" data-bind="click: $parent.grant, disable: $parent.submitting">Grant</button>
            <!-- /ko -->
        </td>
    </tr>
    </tbody>
</table>
<!-- /ko -->
<ul class="list-unstyled" data-bind="foreach: validations">
    <li class="validationMessage" data-bind="text: $data"></li>
</ul>
