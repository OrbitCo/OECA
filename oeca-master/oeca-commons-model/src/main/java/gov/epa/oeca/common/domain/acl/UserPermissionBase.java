package gov.epa.oeca.common.domain.acl;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;

@MappedSuperclass
public abstract class UserPermissionBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "entity_id", nullable = false)
    String entityId;

    /** Used to set application-specific entity details after application calls ACL services **/
    @Transient
    Object entity;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "module_permission_id", nullable = false)
    ModulePermission modulePermission;

    @Override
    public String toString() {
        return "UserPermissionBase{" +
                "user='" + user + '\'' +
				", entityId='" + entityId + '\'' +
                ", modulePermission='" + modulePermission + '\'' +
                "} ";
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getEntityId() { return entityId; }

    public void setEntityId(String entityId) { this.entityId = entityId; }

    public Object getEntity() { return entity; }

    public void setEntity(Object entity) { this.entity = entity; }

    public ModulePermission getModulePermission() { return modulePermission; }

    public void setModulePermission(ModulePermission modulePermission) { this.modulePermission = modulePermission; }
}
