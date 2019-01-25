<script type="text/html" id="pending-requests-child-row">
    <div class="list-group">
        <div class="list-group-item">
            <div class="row">
                <div class="col-sm-2">
                    <label class="control-label">Request Date</label>
                </div>
                <div class="col-sm-2">
                    <label class="control-label">Role</label>
                </div>
                <div class="col-sm-2">
                    <label class="control-label">Actions</label>
                </div>
            </div>
        </div>
        <!-- ko foreach: data.permissions -->
            <div class="list-group-item">
                <div class="row">
                    <div class="col-sm-2" data-bind="text: oeca.utils.formatDateTime(requestDate)"></div>
                    <div class="col-sm-2" data-bind="text: oeca.acl.utils.permissionDisplay($data, $parent.entityConfig)"></div>
                    <div class="col-sm-2">
                        <button class="btn btn-success-outline btn-xs" data-bind="click: function(){$parent.approve($data)}">Approve</button>
                        <button class="btn btn-danger-outline btn-xs" data-bind="click: function(){$parent.reject($data)}">Reject</button>
                    </div>
                </div>
            </div>
        <!-- /ko -->
    </div>
</script>
<!-- ko templateNodeId: 'title' -->
<h1>Pending Requests</h1>
<!-- /ko -->
<!-- ko if: requests -->
<table class="table table-stripes table-condensed dataTable responsive no-wrap"
       data-bind="attr: {id: id},
                datatable: dtConfig(),
                datasource: requests().permissions,
                childRow: {
                    name: 'pending-requests-child-row',
                    vm: PendingPermissionsChildRowController,
                    data: {
                        approve: approve,
                        reject: reject,
                        entityConfig: entityConfig
                    }
                }" style="width: 100%">
</table>
<ul class="list-unstyled" data-bind="foreach: validationMessages">
    <li class="validationMessage" data-bind="text: $data"></li>
</ul>
<!-- /ko -->
<!-- ko ifnot: requests -->
    <span class="fa fa-spinner fa-spin fa-3x"></span>
<!-- /ko -->