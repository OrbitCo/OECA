/**
 * Created by smckay on 9/1/2017.
 */
var PermissionHistoryController = function(data, params) {
    var self = this;
    self.entityId = params.entityId;
    self.entityConfig = params.entityConfig;
    self.history = ko.observableArray([]);
    self.loadHistory = function() {
        oeca.acl.entity.history(self.entityConfig.api, self.entityId()).success(function(history) {
            ko.mapping.fromJS(history, {
                '': {
                    create: function(options) {
                        return new UserPermission(options.data);
                    }
                }
            }, self.history);
        });
    };
    self.entityColumns = function() {
        return self.entityConfig.additionalColumns || [{
                name: 'entityId',
                orderable: true,
                title: 'ID',
                data: 'entityId',
                render: $.fn.dataTable.render.ko.observable()
            }];
    }
    self.dtConfig = function() {
        return {
            processing: false,
            lengthMenu: [[10,25,50,100], [10,25,50,100]],
            responsive: {
                details: false
            },
            autoWidth: false,
            initComplete: function() {
                $("#table-title").append($("#title"));
                $("#search-div").css("margin-top", '20px');
                $("#search-div").append("<br/>");
            },
            columns: [
                {
                    name: 'requestDate',
                    orderable: true,
                    title: 'Request Date',
                    data: 'requestDate',
                    render: $.fn.dataTable.render.ko.computed(function(data, type) {
                        return (type == 'display') ? oeca.utils.formatDateTime(data) : data;
                    }),
                    responsivePriority: 10,
                    width: '15%'
                },
                {
                    name: 'userName',
                    orderable: true,
                    title: 'User',
                    data: 'user.userId',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 10,
                    width: '10%'
                },
                {
                    name: 'action',
                    orderable: true,
                    title: 'Action',
                    data: null,
                    render: $.fn.dataTable.render.ko.computed(function(data) {
                        return oeca.acl.utils.permissionDisplay(data, self.entityConfig) + ' Permission ' + ((data.requestAction().toLowerCase() === 'revoke') ? 'Revoked' :
                        	((data.requestStatus().toLowerCase() === 'approved') ? 'Granted' : data.requestStatus()));
                    }),
                    responsivePriority: 10,
                    width: '25%'
                },
                {
                    name: 'reviewDate',
                    orderable: true,
                    title: 'Review Date',
                    data: 'reviewDate',
                    render: $.fn.dataTable.render.ko.computed(function(data, type) {
                        return (type == 'display') ? oeca.utils.formatDateTime(data) : data;
                    }),
                    responsivePriority: 20,
                    width: '15%'
                },
                {
                    name: 'reviewer',
                    orderable: true,
                    title: 'Reviewer',
                    data: 'reviewer.userId',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 20,
                    width: '10%'
                },
                {
                    name: 'reason',
                    orderable: false,
                    title: 'Comment',
                    data: 'reviewComment',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 30,
                    width: '25%'
                }
            ],
            order: [[0, 'desc']],
            dom: '<"#table-title.col-sm-6"><"#search-div.pull-right col-sm-6"f><t><"col-sm-4"i><"col-sm-4"l><"col-sm-4 pull-right"p>',
            "language": {
                "emptyTable": "There is currently no history for this Operator."
            }
        };
    };
    self.loadHistory();
}
ko.components.register('permission-history', {
    viewModel: {
        viewModelClass: PermissionHistoryController
    },
    template: {
        component: 'permission-history'
    }
})