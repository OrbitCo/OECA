package gov.epa.oeca.common.domain.acl;

public class UserPermissionSearchCriteria {

    String userId;
    UserDomain userDomain;
    String entityId;
    Module module;
    EntityType entityType;
    PermissionType permissionType;
    NotificationStatus notificationStatus;

    public UserPermissionSearchCriteria() {}

    @Override
    public String toString() {
        return "UserPermissionSearchCriteria{" +
                "userId='" + userId + '\'' +
                ", userDomain='" + userDomain + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityType='" + entityType + '\'' +
				", module='" + module + '\'' +
				", permissionType='" + permissionType + '\'' +
                ", notificationStatus='" + notificationStatus + '\'' +
                "} ";
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

    public String getEntityId() { return entityId; }

    public void setEntityId(String entityId) { this.entityId = entityId; }

    public EntityType getEntityType() { return entityType; }

    public void setEntityType(EntityType entityType) { this.entityType = entityType; }

    public Module getModule() { return module; }

    public void setModule(Module module) { this.module = module; }

    public PermissionType getPermissionType() { return permissionType; }

    public void setPermissionType(PermissionType permissionType) { this.permissionType = permissionType; }

	public NotificationStatus getNotificationStatus() { return notificationStatus; }

	public void setNotificationStatus(NotificationStatus notificationStatus) { this.notificationStatus = notificationStatus; }

}
