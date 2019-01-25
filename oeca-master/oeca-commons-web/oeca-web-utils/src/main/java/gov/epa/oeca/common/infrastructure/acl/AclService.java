package gov.epa.oeca.common.infrastructure.acl;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.acl.*;

import java.util.List;
import java.util.Map;

public interface AclService {

    List<PermissionType> retrieveApplicablePermissions(Module module, EntityType entityType) throws ApplicationException;

    List<PermissionType> retrieveUserPermissionTypes(String userId, UserDomain userDomain, String entityId,
                                                     Module module, EntityType entityType) throws ApplicationException;

    List<UserPermission> retrieveUserPermissions(String userId, UserDomain userDomain, String entityId,
                                                 Module module, EntityType entityType, PermissionType permissionType) throws ApplicationException;

    Boolean checkUserPermission(String userId, UserDomain userDomain, String entityId,
                                Module module, EntityType entityType, PermissionType permissionType) throws ApplicationException;

    List<UserPermissionRequest> retrievePendingRequests(String userId, UserDomain userDomain, String entityId,
                                                        Module module, EntityType entityType,
                                                        NotificationStatus notificationStatus) throws ApplicationException;

    List<UserPermissionRequest> retrieveCompletedRequests(String userId, UserDomain userDomain, String entityId,
                                                          Module module, EntityType entityType,
                                                          NotificationStatus notificationStatus) throws ApplicationException;

    /**
     * Pulls back a list of pending requests across all entities a user has access to view requests for.
     * @param userId (required) user id of the user that has permission to view these requests
     * @param userDomain (required) domain of the user that has permission to view these requests
     * @param module (required) application module the requests are for
     * @param entityType (optional) filters requests by entity type
     * @param permissionType (required) the permission type the user needs to have access to see pending requests
     *                       (usually Manage)
     * @return
     */
    List<UserPermissionRequest> retrievePendingRequestsForAllEntities(String userId, UserDomain userDomain,
                                                                      Module module, EntityType entityType,
                                                                      PermissionType permissionType);

    /**
     * Pulls back a list of penidng requests across all entities for a module.  This method is intended to be used by an
     * admin user who has permission to manage all sites but does not use acl permissions
     * @param module (required) application module the requests are for
     * @param entityType (optional) filters requests by entity type
     * @return Tuple first parameter will be a UserPermissionRequest the second will be count of users who have
     * permissionType or null if permissionType was not provided.
     */
    List<UserPermissionRequest> retrievePendingRequestsForAllEntities(Module module, EntityType entityType);

    Map<String, Long> retrievePermissionCountPerEntity(List<String> entityIds, PermissionType permissionType);

    UserPermissionRequest createAccessRequest(String userId, UserDomain userDomain, String entityId, Module module,
                                              EntityType entityType, PermissionType permissionType) throws ApplicationException;

    UserPermissionRequest approveAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                               String reviewComment) throws ApplicationException;

    UserPermissionRequest rejectAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                              String reviewComment) throws ApplicationException;

    UserPermissionRequest cancelAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                              String reviewComment) throws ApplicationException;

    UserPermissionRequest setNotificationStatus(Long requestId, NotificationStatus notificationStatus, Long notificationRetries) throws ApplicationException;

    UserPermission grantAccess(String userId, UserDomain userDomain, String entityId, Module module,
                               EntityType entityType, PermissionType permissionType,
                               String reviewerUserId, UserDomain reviewerUserDomain,
                               String reviewComment) throws ApplicationException;

    void revokeAccess(String userId, UserDomain userDomain, String entityId, Module module,
                      EntityType entityType, PermissionType permissionType,
                      String reviewerUserId, UserDomain reviewerUserDomain,
                      String reviewComment) throws ApplicationException;

    void deleteForEntity(String entityId, Module module, EntityType entityType) throws ApplicationException;
}
