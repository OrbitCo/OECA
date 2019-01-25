/**
 * Created by smckay on 8/7/2017.
 *
 * Depends on acl-models.js
 */
var CurrentPermissionsController = function(data, params) {
    var self = this;
    self.entityConfig = params.entityConfig;
    self.id = params.id || 'current-permissions-id-' + oeca.utils.nextSequence();
    self.entityId = params.entityId;
    self.subscriptions = [];
    if(params.reloadUsers != undefined) {
        self.subscriptions.push(params.reloadUsers.subscribe(function () {
            self.loadUsers();
        }));
    }
    self.users = ko.observableArray([]);
    self.loadUsers = function() {
        self.users.removeAll();
        $.when(
            oeca.acl.entity.current(self.entityConfig.api, self.entityId())
        ).done(function (permissions) {
                var userMap = {};
                self.searchCriteria = ko.observable(new Contact({
                    dataflow: params.dataflow,
                    roleStatus: "Active",
                    roleIds: params.searchRoleIds
                }));
                ko.utils.arrayForEach(permissions, function (permission) {
                    if (userMap[permission.user.userId] == undefined) {
                        var user = new Contact($.extend({
                            roleId: null
                        }, permission.user));
                        userMap[permission.user.userId] = ko.observable(new UserEntityPermissions(user));
                        self.searchCriteria().userId = permission.user.userId;
                        var dtCriteria = {
                            config: data,
                            criteria: self.searchCriteria
                        };
                        var criteriaJson = ko.mapping.toJSON(dtCriteria);
                        $.ajax({
                            url: config.registration.ctx + '/api/registration/v1/user/search',
                            contentType: 'application/json',
                            type: 'post',
                            data: criteriaJson,
                            dataType: 'json',
                            beforeSend: oeca.xhrSettings.setJsonAcceptHeader,
                            success: function (results) {
                                if (results.results !== null) {
                                    userMap[permission.user.userId]().user.firstName(results.results[0].user.firstName);
                                    userMap[permission.user.userId]().user.lastName(results.results[0].user.lastName);
                                    $.each(results.results, function(index,result) {
                                        userMap[permission.user.userId]().user.roleId(result.role.type.code);
                                        return $.inArray(result.role.type.code, params.signatoryRoleIds) == -1;
                                    });
                                }
                            },
                            error: function (res) {
                                console.log("error retrieving user info");
                                console.log(res);
                            }
                        });
                    }
                    userMap[permission.user.userId]().add(permission);
                });
                for (var i in userMap) {
                    self.users.push(userMap[i]());
                }
            });
    }
    self.loadUsers();

    self.change = function(user) {
        params.callback(user.user);
    }
    self.grantPermissions = function() {
        params.grantPermissions();
    }
    self.dtConfig = function() {
        return {
            columns: [
                {
                    name: 'userId',
                    title: 'User Name',
                    data: 'user.userId',
                    responsivePriority: 10,
                    render: $.fn.DataTable.render.ko.observable()
                },
                {
                    name: 'firstName',
                    title: 'First Name',
                    data: 'user.firstName',
                    responsivePriority: 40,
                    render: $.fn.DataTable.render.ko.observable()
                },
                {
                    name: 'lastName',
                    title: 'Last Name',
                    data: 'user.lastName',
                    responsivePriority: 40,
                    render: $.fn.DataTable.render.ko.observable()
                },
                {
                    name: 'requestedPermissions',
                    title: 'Permissions',
                    data: 'requestedPermissions',
                    responsivePriority: 20,
                    render: $.fn.dataTable.render.ko.computed(function(data) {
                        return oeca.acl.utils.permissionsDisplayAsString(data, self.entityConfig);
                    })
                },
                {
                    name: 'actions',
                    title: 'Actions',
                    data: null,
                    orderable: false,
                    responsivePriority: 30,
                    render: $.fn.DataTable.render.ko.actions([
                        {
                            name: 'Change',
                            cssClass: 'btn-primary btn-xs',
                            action: self.change
                        }
                    ], '#' + self.id)
                }
            ],
            responsive: true,
            searching: false,
            info: false,
            lengthChange: false
        }
    }
    self.dispose = function() {
        oeca.common.utils.disposeList(self.subscriptions);
    }
};
ko.components.register('current-permissions', {
    viewModel: {
        viewModelClass: CurrentPermissionsController
    },
    template: {
        component: 'current-permissions'
    }
})