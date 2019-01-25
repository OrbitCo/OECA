delete from oeca_acl_module_permissions where entity_type = 'Operator' and module = 'GMG' and permission_type = 'Submit';
insert into oeca_acl_module_permissions (id, entity_type, module, permission_type) values (3, 'Operator', 'GMG', 'Sign');

insert into oeca_acl_module_permissions (id, entity_type, module, permission_type) values (5, 'Structure', 'GMG', 'View');
insert into oeca_acl_module_permissions (id, entity_type, module, permission_type) values (6, 'Structure', 'GMG', 'Edit');
insert into oeca_acl_module_permissions (id, entity_type, module, permission_type) values (7, 'Structure', 'GMG', 'Sign');
insert into oeca_acl_module_permissions (id, entity_type, module, permission_type) values (8, 'Structure', 'GMG', 'Manage');
