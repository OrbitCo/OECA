/**
 * Created by smckay on 8/4/2017.
 */
var User = function(data) {
    var self = this;
    data = $.extend({
        userId: null,
        userDomain: null
    }, data);
    ko.mapping.fromJS(data, {}, self);
}
var UserPermission = function(data) {
    var self = this;
    data = $.extend({
        id: null,
        user: null,
        entityId: null,
        entity: null,
        modulePermission: null
    }, data);
    self.map = function (data) {
        ko.mapping.fromJS(data, {
            user: {
                create: function (options) {
                    return new User(options.data);
                }
            },
            modulePermissions: {
                create: function (options) {
                    return new ModulePermission(options.data);
                }
            }
        }, self);
    }
    self.map(data);
};
var example = {
    user: {
        userId: 'oeca.test'
    },
    entityId: 1,
    modulePermission: {
        permissionType: 'View'
    },
    reviewComment: null
}
var ModulePermission = function(data) {
    var self = this;
    data = $.extend({
        entityType: null,
        module: null,
        permissionType: null,
        comment: null
    });
    ko.mapping.fromJS(data, {}, self);
};
var UserRequest = function(data) {
    var self = this;
    data = $.extend({
        user: null,
        entityId: null,
        entity: null,
        modulePermissions: []
    });
    ko.mapping.fromJS(data, {
        modulePermissions: {
            create: function(options) {
                return new ModulePermission(options.data);
            }
        }
    }, self);
};
var UserEntityPermissions = function(user, entity, entityId) {
    var self = this;
    self.entity = entity;
    self.user = user;
    self.permissions = ko.observableArray([]);
    self.simplePermissions = ko.observableArray([]);
    self.entityId = entityId;
    self.add = function(permission) {
        self.permissions.push(permission);
        self.simplePermissions.push(permission.modulePermission);
    }
    self.remove = function(permission) {
        var toRemove = self.permissions.lookupById(permission.id);
        self.permissions.remove(toRemove);
    }
    self.requestedPermissions = ko.pureComputed(function() {
        var permissions = [];
        ko.utils.arrayForEach(self.permissions(), function(permission) {
            permissions.push(permission.modulePermission.permissionType);
        });
        return permissions;
    });
    self.maxRequestDate = ko.pureComputed(function () {
        return oeca.common.utils.formatDateTime(
            moment(Math.max.apply(null, ko.utils.arrayMap(self.permissions(), function(e) {
                    return moment(e.requestDate);
                }))));
    });
    self.dispose = function() {
        oeca.utils.dispose(self.requestedPermissions);
        oeca.utils.dispose(self.maxRequestDate);
    }
};
var PermissionsByUserEntity = function(data) {
    var self = this;
    self.permissions = ko.observableArray([]);
    self.permissionsMap = {};
    ko.utils.arrayForEach(data, function(userPermission) {
        var permissionEntry = self.permissionsMap['userId' + userPermission.user.id + '-entityId' + userPermission.entityId];
        if(!permissionEntry) {
            permissionEntry = new UserEntityPermissions(userPermission.user, userPermission.entity, userPermission.entityId);
            self.permissionsMap['userId' + userPermission.user.id + '-entityId' + userPermission.entityId] = permissionEntry;
            self.permissions.push(permissionEntry);
        }
        permissionEntry.add(userPermission);
    });
    self.remove = function(permissions) {
        ko.utils.arrayForEach(permissions, function(userPermission) {
            var permissionEntry = self.permissionsMap['userId' + userPermission.user.id + '-entityId' + userPermission.entityId];
            if(permissionEntry) {
                permissionEntry.remove(userPermission);
                if(permissionEntry.permissions().length == 0) {
                    self.permissions.remove(permissionEntry);
                    self.permissionsMap['userId' + userPermission.user.id + '-entityId' + userPermission.entityId] = undefined;
                    permissionEntry.dispose();
                }
            }
        });
    }
}