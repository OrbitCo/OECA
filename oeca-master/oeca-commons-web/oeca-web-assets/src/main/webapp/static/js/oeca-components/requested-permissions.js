/**
 * Created by smckay on 8/4/2017.
 *
 * Depends on acl-models.js
 */
var RequestedPermissionsController = function(data, params) {
    var self = this;
    self.entityConfig = params.entityConfig;
    self.id = params.id || 'requested-permissions-id-' + oeca.utils.nextSequence();
    self.requests = ko.observableArray([]);
    self.historicalRequests = ko.observableArray([])
    self.loadRequests = function() {
        oeca.acl.user.pending(self.entityConfig.api).success(function(requests) {
            ko.mapping.fromJS(requests, {
                '': {
                    create: function(options) {
                        return new UserPermission(options.data);
                    }
                }
            }, self.requests);
        });
    };
    self.loadRequests();
    self.loadHistoricalRequests = function() {
        oeca.acl.user.history(self.entityConfig.api).success(function(requests) {
           ko.mapping.fromJS(requests, {
               '': {
                   create: function(options) {
                       return new UserPermission(options.data);
                   }
               }
           }, self.historicalRequests);
        });
    }
    self.requestColumns = function() {
        return [
            {
                title: 'Requested Permissions',
                data: 'modulePermissions'
            },
            {
                title: 'Actions',
                data: null,
                render: $.fn.DataTable.render.ko.actions([
                    {
                        name: 'Cancel',
                        cssClass: 'btn-default btn-xs',
                        action: function(data) {
                            oeca.acl.request.cancel(self.entityConfig.api, [data]).success(function() {
                                self.requests.remove(data);
                            });
                        }
                    },
                    {
                        name: 'Request',
                        cssClass: 'btn-primary btn-xs'
                    }
                ], '#' + self.id)
            }
        ]
    }
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
    }
    self.dtConfig = function() {
        return {
            columns: self.additionalColumns().concat(self.requestColumns()),
            responsive: false
        }
    }
    self.historyDtConfig = function() {
        return {
            columns: self.additionalColumns().concat([
                {
                    title: 'Requested Permissions',
                    data: 'modulePermissions'
                },
                {
                    title: 'Comment',
                    data: 'comment'
                },
                {
                    title: 'Response',
                    data: 'result'
                }
            ]),
            responsive: false
        }
    }
};
ko.components.register('requested-permissions', {
    viewModel: {
        viewModelClass: RequestedPermissionsController
    },
    template: {
        component: 'requested-permissions'
    }
});