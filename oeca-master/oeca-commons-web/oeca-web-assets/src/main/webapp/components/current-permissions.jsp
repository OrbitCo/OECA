<!-- ko templateNodeId: 'title' -->
<h2>Current Users</h2>
<!-- /ko -->
<table class="table table-stripes table-condensed dataTable responsive no-wrap" style="width: 100%"
       data-bind="attr: {id: id},
                  datatable: dtConfig(),
                  datasource: users">
</table>