var UserSearchController = function(data, params) {
    var self = this;
    self.panel = ko.observable("search");
    self.inviteCallback = params.inviteCallback;
    self.showSearch = ko.pureComputed({
        read: function() {
            return self.panel() == 'search';
        },
        write: function(val) {
            if(val) {
                self.panel('search');
            }
            else if(self.panel() == 'search') {
                self.panel(null);
            }
        }
    });
    self.loadResults = ko.observable(null);
    self.showResults = ko.pureComputed({
        read: function() {
            return self.panel() == 'results';
        },
        write: function(val) {
            if(val) {
                self.panel('results');
            }
            else if(self.panel() == 'results') {
                self.panel(null);
            }
        }
    });
    self.labels = params.titles;
    self.searchCriteria = ko.observable(params.defaultCriteria || new Contact({
        organization: null,
        address: null,
        dataflow: params.dataflow(),
        roleId: params.roleId,
        roleIds: params.searchRoleIds
    }));
    self.searchCriteria().userId = ko.observable(null).extend({minLength: 3});
    self.searchCriteria().lastName = ko.observable(null).extend({minLength: 3});
    self.searchCriteria().organization = ko.observable(null).extend({minLength: 3});
    self.searchCriteria().address = ko.observable(null).extend({minLength: 3});
    self.searchCriteria().email = ko.observable(null).extend({minLength: 3});
    self.searchCriteria.extend({
        requiresOneOf: [
            self.searchCriteria().userId,
            self.searchCriteria().firstName,
            self.searchCriteria().middleInitial,
            self.searchCriteria().lastName,
            self.searchCriteria().organization,
            self.searchCriteria().address,
            self.searchCriteria().email
        ]
    });
    self.searchCriteria().errors = ko.validation.group(self.searchCriteria, {deep: true});
    self.resetSearchCriteria = function() {
        self.searchCriteria().userId(null);
        self.searchCriteria().firstName(null);
        self.searchCriteria().lastName(null);
        self.searchCriteria().organization(null);
        self.searchCriteria().address (null);
        self.searchCriteria().middleInitial(null);
        self.searchCriteria().email(null);
        self.showSearch(true);
    };

    self.helpText = params.helpText;
    self.showRoleColumn = params.showRoleColumn;
    self.id = params.id;
    if(!self.id) {
        throw "id is required for user search component";
    }
    self.roles = params.roles;
    self.dtConfig = function () {
        return {
            columns: [
                {
                    name: 'user.userId',
                    'orderable': true,
                    'data': 'userId',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 20,
                    width: '15%'
                },
                {
                    name: 'user.firstName',
                    'orderable': true,
                    'data': 'firstName',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 80,
                    width: '10%'
                },
                {
                    name: 'user.lastName',
                    'orderable': true,
                    'data': 'lastName',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 70,
                    width: '10%'
                },
                {
                    name: 'org',
                    'orderable': true,
                    'data': 'organization',
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 60,
                    width: '15%'
                },
                {
                    name: 'role',
                    'orderable': false,
                    'data': 'role',
                    visible: (self.showRoleColumn == true),
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 40,
                    width: '15%'
                },
                {
                    name: 'address',
                    'orderable': true,
                    'data': 'address',
                    visible: (self.showRoleColumn == true),
                    render: $.fn.dataTable.render.ko.observable(),
                    responsivePriority: 100,
                    width: '255%'
                },
                {
                    name: 'action',
                    orderable: false,
                    render: $.fn.dataTable.render.ko.action('Select', self.selectUser, 'btn-primary btn-xs', '#' + self.id + '-search-results'),
                    responsivePriority: 30,
                    width: '10%'
                }
            ],
            dom: '<r><t><"col-sm-4"i><"col-sm-4"l><"col-sm-4 pull-right"p>',
            language: {
                emptyTable: 'No users matched your search criteria.',
                processing: 'Processing... <span class="fa fa-spinner fa-spin"></span>'
            },
            searching: false,
            processing: true,
            serverSide: true,
            responsive: {
                details: false
            },
            lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]],
            autoWidth: false,
            ajax: function (data, callback, settings) {
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
                        ko.mapping.fromJS(results.results, {
                            '': {
                                create: function (options) {
                                    return new Contact({
                                        userId: options.data.user.userId,
                                        firstName: options.data.user.firstName,
                                        middleInitial: options.data.user.middleInitial,
                                        lastName: options.data.user.lastName,
                                        email: options.data.organization.email,
                                        organization: options.data.organization.organizationName,
                                        role: self.roles[options.data.role.type.code],
                                        roleId: options.data.role.type.code,
                                        address: options.data.organization.mailingAddress1 +
                                        (options.data.organization.mailingAddress2 !== null ? ' ' + options.data.organization.mailingAddress2 : '') +
                                        ', ' + options.data.organization.city +
                                        ', ' + options.data.organization.state.code +
                                        ' ' + options.data.organization.zip +
                                        ', ' + options.data.organization.country.code
                                    });
                                }
                            }
                        }, self.searchResults);
                        callback({
                            data: self.searchResults() || [],
                            draw: data.draw,
                            recordsTotal: results.totalCount,
                            recordsFiltered: results.totalCount
                        });
                    },
                    error: function (res) {
                        console.log("error searching for users");
                        console.log(res);
                    }
                })
            }
        }
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
    };
    self.search = function(page) {
        var userSearchErrors = self.searchCriteria().errors;
        if (userSearchErrors().length > 0) {
            userSearchErrors.showAllMessages();
            return;
        }
        if(!self.loadResults()) {
            self.loadResults(true);
        }
        else {
            $('#' + self.id + '-search-results').DataTable().ajax.reload(null,true);
        }
        self.showResults(true);
    };
    self.selectedUser = ko.observable(null);
    self.searchResults = ko.observableArray([]);
    self.selectUser = function(user) {
        self.selectedUser(user);
        params.callback(self.selectedUser());
    };
    self.subscriptions = [];
    self.subscriptions.push(params.dataflow.subscribe(function() {
        self.resetSearchCriteria();
    }));
    self.dispose = function() {
        oeca.common.utils.disposeList(self.subscriptions);
    }
};

ko.components.register('user-search', {
    viewModel: {
        viewModelClass: UserSearchController
    },
    template: {
        component: 'user-search'
    }
});
