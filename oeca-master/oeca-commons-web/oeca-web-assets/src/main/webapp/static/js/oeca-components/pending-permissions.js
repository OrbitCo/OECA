/**
 * Created by smckay on 8/4/2017.
 *
 * Depends on acl-models.js
 */
var PendingPermissionRequestsController = function(data, params) {
    var self = this;
    self.entityConfig = params.entityConfig;
    self.showTableInfo = params.showTableInfo;
    self.id = params.id || 'pending-permissions-id-' + oeca.utils.nextSequence();
    self.emptyTableText = params.emptyText || null;
    self.subscriptions = [];
    if(params.reloadRequests != undefined) {
        self.subscriptions.push(params.reloadRequests.subscribe(function () {
            self.loadRequests();
        }));
    }
    self.requests = ko.observable(null);
    self.validationMessages = ko.observableArray([]);
    self.loadRequests = function() {
        self.requests(null);
        var requestCall = params.entityId ?
            oeca.acl.entity.pending(self.entityConfig.api, ko.utils.unwrapObservable(params.entityId)) :
            oeca.acl.request.allPending(self.entityConfig.api)
        requestCall.success(function(requests) {
            self.requests(new PermissionsByUserEntity(requests));
            //publish some information about the update for parent components
            postal.channel('acl').publish('pendingPermissions.requests-loaded.' + self.id, {
                requestCount: requests.length
            });
        });
    };
    self.loadRequests();
    //postal event to refresh
    self.subscriptions.push(postal.channel('acl').subscribe('pendingPermissions.' + (params.entityId ?
        ko.utils.unwrapObservable(params.entityId) : '*'), function(){
        self.loadRequests();
    }));

    self.subscriptions.push(postal.channel('acl').subscribe('*', function(data, envelope) {
        if(envelope.topic == 'request' || envelope.topic == 'cancel' ||
            envelope.topic == 'approve' || envelope.topic == 'reject' || envelope.topic == 'revoke') {
            self.loadRequests();
        }
    }));

    self.additionalColumns = function() {
        if(self.entityConfig.additionalColumns && self.entityConfig.additionalColumns.length > 0) {
            return self.entityConfig.additionalColumns;
        }
        else {
            return [{
                title: 'ID',
                data: 'entityId'
            }];
        }
    };
    self.dtConfig = function() {
        return {
            columns: [
                {
                    className: 'details-control',
                    orderable: false,
                    data: null,
                    render: $.fn.dataTable.render.ko.templateInline('<span class="glyphicon glyphicon-plus-sign"></span>'),
                    responsivePriority: 1
                },
                {
                    title: 'Request Date',
                    data: 'maxRequestDate',
                    responsivePriority: 10,
                    render: $.fn.DataTable.render.ko.observable()
                },
                {
                    title: 'User Name',
                    data: 'user.userId',
                    responsivePriority: 10,
                    render: $.fn.DataTable.render.ko.observable()
                }
            ].concat(self.additionalColumns()).concat([
                {
                    title: 'Requested Permissions',
                    data: 'requestedPermissions',
                    responsivePriority: 20,
                    render: $.fn.dataTable.render.ko.computed(function(data) {
                        return oeca.acl.utils.permissionsDisplayAsString(data, self.entityConfig);
                    })
                },
                {
                    title: 'Actions',
                    data: null,
                    responsivePriority: 10,
                    render: $.fn.DataTable.render.ko.actions([
                        {
                            name: 'Approve All',
                            cssClass: 'btn-success-outline btn-xs',
                            action: self.approve
                        },
                        {
                            name: 'Reject All',
                            cssClass: 'btn-danger-outline btn-xs',
                            action: self.reject
                        }
                    ], '#' + self.id)
                }
            ]),
            responsive: {
                details: false
            },
            searching: self.showTableInfo ? self.showTableInfo : false,
            info: self.showTableInfo ? self.showTableInfo : false,
            lengthChange: self.showTableInfo ? self.showTableInfo : false,
            "language": {
                "emptyTable": self.emptyTableText || "There are currently no pending requests."
            }
        }
    }
    self.approve = function(request) {
        self.entityConfig.validation('Approve', request.permissions || [request]).done(function(messages) {
            self.validationMessages(messages);
            if(self.validationMessages().length == 0) {
                openModal('approve-permission-request', undefined, {
                    data: {
                        request: request,
                        entityConfig: self.entityConfig,
                        successCallback: function (results) {
                            if (params.callback) {
                                params.callback();
                            }
                        }
                    },
                    settings: {}
                });
            }
        });
    };
    self.reject = function(request) {
        self.entityConfig.validation('Reject', request.permissions || [request]).done(function(messages) {
            self.validationMessages(messages);
            if (self.validationMessages().length == 0) {
                openModal('reject-permission-request', undefined, {
                    data: {
                        request: request,
                        entityConfig: self.entityConfig,
                        successCallback: function (results) {
                            if (params.callback) {
                                params.callback();
                            }
                        }
                    },
                    settings: {}
                });
            }
        });
    }
    self.dispose = function() {
        oeca.common.utils.disposeList(self.subscriptions);
    }
};
var PendingPermissionsChildRowController = function (data) {
    var self = this;
    self.data = data.data;
    self.approve = data.approve;
    self.reject = data.reject;
    self.entityConfig = data.entityConfig;
    self.closeChildRow = data.closeChildRow;
}
var ProcessPermissionController = function(data, params) {
    var self = this;
    self.modalControl = params.modalControl;
    self.reviewComment = ko.observable(null);
    self.request = ko.observable(null);
    self.permissions = ko.pureComputed(function() {
        if (self.request().permissions()) {
            var simplePermissions = '';
            ko.utils.arrayForEach(self.request().permissions(), function(permission) {
                simplePermissions = simplePermissions + permission.modulePermission.permissionType() + ', ';
            });
            return simplePermissions.substring(0, simplePermissions.length - 2);
        }
        else {
            return self.request.modulePermission.permissionType;
        }
    });
    self.entityName = ko.pureComputed(function() {
        if(self.request() && self.entityConfig.entityName) {
            return self.entityConfig.entityName(self.request().entity);
        }
        else if(self.request()) {
            return 'entity ' + self.request().entityId();
        }
    });
    self.reject = function() {
        self.processApproveReject(false);
    }
    self.approve = function() {
        self.processApproveReject(true);
    }
    self.processApproveReject = function(isApprove) {
        var permissionList = ko.utils.unwrapObservable(self.request().permissions) || [self.request()];
        if (self.reviewComment()) {
            ko.utils.arrayForEach(permissionList, function (request) {
                request.reviewComment(self.reviewComment());
            });
        }
        var apiMethod = isApprove ? oeca.acl.request.approve : oeca.acl.request.reject;
        apiMethod(self.entityConfig.api, permissionList).success(function (result) {
            oeca.notifications.showSuccessMessage("Request(s) successfully " + (isApprove ? "approved" : "rejected"));
            self.modalControl.closeModal(isApprove ? 'approve' : 'reject')
            if (self.successCallback) {
                self.successCallback(result);
            }
        });
    }
    self.refresh = function(data) {
        /*
        the request passed into the modal can have different structures clear out the current structure so we don't have
        any fields left over from the old structure
         */
        self.request(null);
        self.request(new UserPermission(data.request));
        self.entityConfig = data.entityConfig;
        self.successCallback = data.successCallback;
        self.reviewComment(null);
    }
}
ko.components.register('pending-permission-requests', {
    viewModel: {
        viewModelClass: PendingPermissionRequestsController
    },
    template: {
        component: 'pending-permission-requests'
    }
});
ko.components.register('approve-permission-request', {
    viewModel: {
        modal: true,
        viewModelClass: ProcessPermissionController
    },
    template: {
        modal: 'approve-permission-request'
    }
});
ko.components.register('reject-permission-request', {
    viewModel: {
        modal: true,
        viewModelClass: ProcessPermissionController
    },
    template: {
        modal: 'reject-permission-request'
    }
});