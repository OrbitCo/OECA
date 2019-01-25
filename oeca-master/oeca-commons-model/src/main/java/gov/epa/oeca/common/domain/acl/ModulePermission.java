package gov.epa.oeca.common.domain.acl;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "oeca_acl_module_permissions")
public class ModulePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    EntityType entityType;

    @Column(name = "module", nullable = false)
    @Enumerated(EnumType.STRING)
    Module module;

    @Column(name = "permission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    PermissionType permissionType;

    @Override
    public String toString() {
        return "ModulePermission{" +
                "entityType='" + entityType + '\'' +
				", module='" + module + '\'' +
				", permissionType='" + permissionType + '\'' +
                "} ";
    }

    public EntityType getEntityType() { return entityType; }

    public void setEntityType(EntityType entityType) { this.entityType = entityType; }

    public Module getModule() { return module; }

    public void setModule(Module module) { this.module = module; }

    public PermissionType getPermissionType() { return permissionType; }

    public void setPermissionType(PermissionType permissionType) { this.permissionType = permissionType; }
}
