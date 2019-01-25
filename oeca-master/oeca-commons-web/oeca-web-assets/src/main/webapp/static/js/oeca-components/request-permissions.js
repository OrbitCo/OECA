/**
 * Created by smckay on 8/4/2017.
 *
 * Depends on acl-models.js
 */
var RequestPermissionsController = function(data, params) {
    var self = this;
    self.entityConfig = ko.mapping.toJS(params.entityConfig);
    self.entityId = params.entityId;
    self.entity = params.entity;
    self.userId = ko.utils.unwrapObservable(params.userId);
    self.roleId = ko.utils.unwrapObservable(params.roleId);
    self.permissions = ko.observableArray([]);
    self.reloadPermissions = function() {
    	self.permissions.removeAll();
	    $.when(
	        oeca.acl.entity.pending(self.entityConfig.api, self.entityId(), self.userId),
	        oeca.acl.entity.current(self.entityConfig.api, self.entityId(), self.userId)
	    ).done(function(pendingPermissions, currentPermissions) {
	        var requestMap = {};
	        ko.utils.arrayForEach(self.entityConfig.permissionTypes, function(permissionType) {
	            requestMap[permissionType.value] = null;
	        });
	        ko.utils.arrayForEach(pendingPermissions[0], function(permission) {
	            requestMap[permission.modulePermission.permissionType] = new UserPermission($.extend({
	                requestStatus: null
	            }, permission));
	        });
	        ko.utils.arrayForEach(currentPermissions[0], function(permission) {
	            requestMap[permission.modulePermission.permissionType] = new UserPermission($.extend({
	                requestStatus: null
	            }, permission));
	        });
	        for(var i in requestMap) {
	            if(requestMap[i] == null) {
	                requestMap[i] = new UserPermission({
	                    requestStatus: null,
	                    entityId: self.entityId(),
	                    entity: self.entity(),
	                    modulePermission: {
	                        permissionType: i
	                    },
	                    user: {
	                        userId: self.userId
	                    }
	                })
	            }
	            requestMap[i].submitting = ko.observable(false);
                requestMap[i].available = ko.observable(true);
	            ko.utils.arrayForEach(self.entityConfig.permissionTypeRestrictions, function(restriction) {
	                if (self.roleId == restriction.roleId && ($.inArray(i, restriction.unavailablePermissions) != -1)) {
                        requestMap[i].available = ko.observable(false);
	                }
	            });
	            self.permissions.push(requestMap[i]);
	        }
	    });
    }
    self.reloadPermissions();
    self.subscriptions = [];
    self.subscriptions.push(self.entityId.subscribe(function() {
        self.reloadPermissions();
    }));
    self.dispose = function() {
        oeca.common.utils.disposeList(self.subscriptions);
    }
    self.cancelRequest = function(request) {
        request.submitting(true);
        self.entityConfig.validation('Cancel', [request], self.permissions).done(function(messages) {
            self.validations(messages);
            if(self.validations().length == 0) {
                oeca.acl.request.cancel(self.entityConfig.api, [request]).success(function(results) {
                    //remove some of the fields so it appears like a unrequested object
                    request.map({
                        id: null,
                        requestStatus: null
                    });
                    oeca.notifications.showInfoMessage("Your request has been canceled.");
                    request.submitting(false);
                });
            }
            else {
                request.submitting(false);
            }
        });
    };
    self.removeAccess = function(request) {
        request.submitting(true);
        self.entityConfig.validation('Revoke', [request], self.permissions).done(function(messages) {
            self.validations(messages);
            if(self.validations().length == 0) {
                oeca.acl.request.revoke(self.entityConfig.api, [request]).success(function (results) {
                    //remove some of the fields so it appears like a unrequested object
                    request.map({
                        id: null,
                        requestStatus: null
                    });
                    oeca.notifications.showSuccessMessage("Your permission has been revoked.");
                    request.submitting(false);
                });
            }
            else {
                request.submitting(false);
            }
        });
    }
    self.validations = ko.observableArray([]);
    self.requestAccess = function(request) {
        request.submitting(true);
        self.entityConfig.validation('Request', [request], self.permissions).done(function(messages) {
            self.validations(messages);
            if(self.validations().length == 0) {
                oeca.acl.request.submit(self.entityConfig.api, [request]).success(function (results) {
                    ko.utils.arrayForEach(results, function (result) {
                        self.permissions.lookupByProp(result.modulePermission.permissionType, 'modulePermission.permissionType')
                            .map(result);
                    });
                    oeca.notifications.showInfoMessage("Your request has been sent.");
                    request.submitting(false);
                });
            }
            else {
                request.submitting(false);
            }
        });
    }
}
ko.components.register('request-permissions', {
    viewModel: {
        viewModelClass: RequestPermissionsController
    },
    template: {
        component: 'request-permissions'
    }
});