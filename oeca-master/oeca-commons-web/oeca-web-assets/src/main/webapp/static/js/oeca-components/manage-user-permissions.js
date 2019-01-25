var ManageUserPermissionsController = function(data, params) {
    var self = this;
    self.id = params.id || 'manage-user-permissions-id-' + oeca.utils.nextSequence();
    self.entityConfig = params.entityConfig;
    self.user = params.user;
    self.entityId = params.entityId;
    self.entityAclId = params.entityAclId;
    self.entity = params.entity;
    self.entityName = params.entityName;
    self.successCallback = params.successCallback;
    self.currentPermissions = ko.observableArray([]);
    self.availablePermissions = ko.observableArray([]);
    self.submitting = ko.observable(false);
    self.validations = ko.observableArray([]);
    self.reloadPermissions = function() {
        var entityConfig = ko.mapping.toJS(params.entityConfig);
        $.when(
            oeca.acl.entity.pending(entityConfig.api, params.entityAclId(), params.user().userId()),
            oeca.acl.entity.current(entityConfig.api, params.entityAclId(), params.user().userId())
        ).done(function(pendingPermissions, currentPermissions) {
                self.currentPermissions.removeAll();
                self.availablePermissions.removeAll();
                //reset validations
                self.validations([]);
                var currentRequestMap = {};
                var requestMap = {};
                ko.utils.arrayForEach(entityConfig.permissionTypes, function(permissionType) {
                    ko.utils.arrayForEach(entityConfig.permissionTypeRestrictions, function(restriction) {
                        if (params.user().roleId() != restriction.roleId || ($.inArray(permissionType.value, restriction.unavailablePermissions) == -1)) {
                            currentRequestMap[permissionType.value] = null;
                            requestMap[permissionType.value] = null;
                        }
                    });

                });
                ko.utils.arrayForEach(pendingPermissions[0], function(permission) {
                    requestMap[permission.modulePermission.permissionType] = new UserPermission($.extend({
                        requestStatus: null,
                        reviewComment: null
                    }, permission));
                });
                ko.utils.arrayForEach(currentPermissions[0], function(permission) {
                    currentRequestMap[permission.modulePermission.permissionType] = new UserPermission($.extend({
                        requestStatus: null,
                        reviewComment: null
                    }, permission));
                });
                for(var i in currentRequestMap) {
                    if(currentRequestMap[i] !== null) {
                        requestMap[i] = false;
                        self.currentPermissions.push(currentRequestMap[i]);
                    }
                }
                for(var i in requestMap) {
                    if (requestMap[i] !== false) {
                        if(requestMap[i] == null) {
                            requestMap[i] = new UserPermission({
                                reviewComment: null,
                                entityId: params.entityAclId(),
                                entity: params.entity(),
                                modulePermission: {
                                    permissionType: i
                                },
                                user: {
                                    userId: params.user().userId()
                                }
                            })
                        }
                        self.availablePermissions.push(requestMap[i]);
                    }
                }
            });
        self.submitting(false);
    }
    self.reloadPermissions();

    self.subscriptions = [];
    self.subscriptions.push(params.user.subscribe(function() {
        self.reloadPermissions();
    }));
    self.subscriptions.push(params.entityAclId.subscribe(function() {
        self.reloadPermissions();
    }));
    self.subscriptions.push(params.entityConfig.subscribe(function() {
        self.reloadPermissions();
    }));
    self.revoke = function(request) {
        self.submitting(true);
        var entityConfig = ko.mapping.toJS(params.entityConfig);
        entityConfig.validation('Revoke', [request], self.currentPermissions).done(function(messages) {
            self.validations(messages);
            if(self.validations().length == 0) {
                oeca.acl.request.revoke(entityConfig.api, [request]).success(function(results) {
                    oeca.notifications.showSuccessMessage("User permission has been revoked.");
                    request.id(null);
                    self.currentPermissions.remove(request);
                    self.availablePermissions.push(request);
                    self.submitting(false);
                    if(self.successCallback) {
                        self.successCallback(results, true);
                    }
                })
            }
            else {
                self.submitting(false);
            }
        });
    }
    self.grant = function(request) {
        self.submitting(true);
        var entityConfig = ko.mapping.toJS(params.entityConfig);
        entityConfig.validation('Grant', [request], self.currentPermissions).done(function(messages) {
            self.validations(messages);
            if(self.validations().length == 0) {
                oeca.acl.request.grant(entityConfig.api, [request]).success(function(results) {
                    oeca.notifications.showInfoMessage("User permission has been granted.");
                    ko.utils.arrayForEach(results, function(result) {
                        self.currentPermissions.push(new UserPermission($.extend({
                            requestStatus: null,
                            reviewComment: null
                        }, result)));
                    });
                    self.availablePermissions.remove(request);
                    self.submitting(false);
                    if(self.successCallback) {
                        self.successCallback(results, true);
                    }
                });
            } else {
                self.submitting(false);
            }
        });
    };
    self.approve = function(request) {
        self.submitting(true);
        var entityConfig = ko.mapping.toJS(params.entityConfig);
        entityConfig.validation('Approve', [request], self.currentPermissions).done(function(messages) {
            self.validations();
            if(self.validations().length == 0) {
                openModal('approve-permission-request', undefined, {
                    data: {
                        request: ko.mapping.toJS(request),
                        entityConfig: params.entityConfig,
                        successCallback: function(results) {
                            self.reloadPermissions();
                            if(self.successCallback) {
                                self.successCallback(results, false);
                            }
                        }
                    },
                    settings: {

                    }
                });
            }
            else {
                self.submitting(false);
            }
        });
    };
    self.reject = function(request) {
        self.submitting(true);
        var entityConfig = ko.mapping.toJS(params.entityConfig);
        entityConfig.validation('Reject', [request], self.currentPermissions).done(function(messages) {
            self.validations(messages);
            if(self.validations().length == 0) {
                openModal('reject-permission-request', undefined, {
                    data: {
                        request: ko.mapping.toJS(request),
                        entityConfig: params.entityConfig,
                        successCallback: function(results) {
                            self.reloadPermissions();
                            if(self.successCallback) {
                                self.successCallback(results, false);
                            }
                        }
                    },
                    settings: {

                    }
                });
            }
            else {
                self.submitting(false);
            }
        });
    }
    self.dispose = function() {
        oeca.common.utils.disposeList(self.subscriptions);
    }
}

ko.components.register('manage-user-permissions', {
    viewModel: {
        viewModelClass: ManageUserPermissionsController
    },
    template: {
        component: 'manage-user-permissions'
    }
});