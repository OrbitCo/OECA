package gov.epa.oeca.common.infrastructure.acl;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.acl.*;
import gov.epa.oeca.common.infrastructure.persistence.AclRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ktucker
 */
@Service("aclService")
@Transactional
public class AclServiceImpl implements AclService {

    private static final Logger logger = LoggerFactory.getLogger(AclServiceImpl.class);

    @Autowired
    AclRepository aclRepository;

    @Override
    public List<PermissionType> retrieveApplicablePermissions(Module module, EntityType entityType) throws ApplicationException {
        try {
            return aclRepository.retrieveApplicablePermissions(module, entityType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<PermissionType> retrieveUserPermissionTypes(String userId, UserDomain userDomain, String entityId, Module module,
                                                            EntityType entityType) throws ApplicationException {
        try {
            List<UserPermission> userPermissions = aclRepository.retrieveAccess(userId, userDomain, entityId, module,
                    entityType, null);
            List<PermissionType> permissionTypes = new ArrayList<>();
            for (UserPermission userPermission : userPermissions) {
                permissionTypes.add(userPermission.getModulePermission().getPermissionType());
            }
            return permissionTypes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<UserPermission> retrieveUserPermissions(String userId, UserDomain userDomain, String entityId,
                                                        Module module, EntityType entityType, PermissionType permissionType) throws ApplicationException {
        try {
            return aclRepository.retrieveAccess(userId, userDomain, entityId, module, entityType, permissionType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Boolean checkUserPermission(String userId, UserDomain userDomain, String entityId, Module module,
                                       EntityType entityType, PermissionType permissionType) throws ApplicationException {
        try {
            List<UserPermission> userPermissions = aclRepository.retrieveAccess(userId, userDomain, entityId, module, entityType, permissionType);
            return userPermissions.size() == 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<UserPermissionRequest> retrievePendingRequests(String userId, UserDomain userDomain, String entityId,
                                                               Module module, EntityType entityType,
                                                               NotificationStatus notificationStatus) throws ApplicationException {
        return aclRepository.retrieveAccessRequests(userId, userDomain, entityId, module, entityType, null, true, notificationStatus);
    }

    @Override
    public List<UserPermissionRequest> retrieveCompletedRequests(String userId, UserDomain userDomain, String entityId,
                                                                 Module module, EntityType entityType,
                                                                 NotificationStatus notificationStatus) throws ApplicationException {
        return aclRepository.retrieveAccessRequests(userId, userDomain, entityId, module, entityType, null, false, notificationStatus);
    }
    @Override
    public List<UserPermissionRequest> retrievePendingRequestsForAllEntities(String userId, UserDomain userDomain,
                                                                             Module module, EntityType entityType,
                                                                             PermissionType permissionType) {
        Validate.notEmpty(userId);
        Validate.notNull(userDomain);
        Validate.notNull(module);
        Validate.notNull(permissionType);
        return aclRepository.retrievePendingAccessRequestsForAllEntities(userId, userDomain, module, entityType,
                permissionType);
    }

    @Override
    public List<UserPermissionRequest> retrievePendingRequestsForAllEntities(Module module, EntityType entityType) {
        Validate.notNull(module);
        return aclRepository.retrievePendingAccessRequestsForAllEntities(module, entityType);
    }

    @Override
    public Map<String, Long> retrievePermissionCountPerEntity(List<String> entityIds, PermissionType permissionType) {
        List<Object[]> results = aclRepository.retrievePermissionCountPerEntity(entityIds, permissionType);
        Map<String, Long> counts = new HashMap<>();
        for(Object[] result : results) {
            counts.put((String) result[0], (Long) result[1]);
        }
        return counts;
    }

    @Override
    public UserPermissionRequest createAccessRequest(String userId, UserDomain userDomain, String entityId, Module module,
                                                     EntityType entityType, PermissionType permissionType) throws ApplicationException {
        try {
            // Check if permission has already been granted
            List<UserPermission> existingPermissions = aclRepository.retrieveAccess(userId, userDomain, entityId,
                    module, entityType, permissionType);
            if (CollectionUtils.isNotEmpty(existingPermissions)) {
                throw new ApplicationException(ApplicationErrorCode.E_PermissionAlreadyExists, "the specified user permission" +
                        " has already been granted");
            }

            // Check if identical pending request already exists
            List<UserPermissionRequest> existingRequests = aclRepository.retrieveAccessRequests(userId, userDomain, entityId,
                    module, entityType, permissionType, true, null);
            if (CollectionUtils.isNotEmpty(existingRequests)) {
                throw new ApplicationException(ApplicationErrorCode.E_RequestAlreadyExists, "the specified user permission request" +
                        " already exists");
            }

            // Get module permission
            ModulePermission modulePermission = aclRepository.retrieveModulePermission(module, entityType, permissionType);
            if (modulePermission == null) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidPermission, "no module permission exists" +
                        " for module " + module + " and entity type " + entityType + " and permission type " + permissionType);
            }

            // Get user, create if user does not exist
            User user = resolveUser(userId, userDomain);

            // Create user permission request
            UserPermissionRequest request = new UserPermissionRequest();
            request.setEntityId(entityId);
            request.setUser(user);
            request.setModulePermission(modulePermission);
            request.setRequestStatus(RequestStatus.Pending);
            request.setRequestAction(RequestAction.Grant);
            request.setRequester(user);
            request.setRequestDate(ZonedDateTime.now());
            request.setNotificationStatus(NotificationStatus.N);
            request.setNotificationRetries(0L);
            aclRepository.saveAccessRequest(request);

            return request;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public UserPermissionRequest approveAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                      String reviewComment) throws ApplicationException {
        return reviewAccessRequest(requestId, reviewerUserId, reviewerUserDomain, reviewComment, RequestStatus.Approved);
    }

    @Override
    public UserPermissionRequest rejectAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                     String reviewComment) throws ApplicationException {
        return reviewAccessRequest(requestId, reviewerUserId, reviewerUserDomain, reviewComment, RequestStatus.Rejected);
    }

    private UserPermissionRequest reviewAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                      String reviewComment, RequestStatus requestStatus) throws ApplicationException {
        try {
            // Get request
            UserPermissionRequest request = aclRepository.retrieveAccessRequest(requestId);
            if (request == null) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidRequest, "no user permission request exists" +
                        " for request ID " + requestId);
            }

            // Only pending requests can be updated
            if (request.getRequestStatus() != RequestStatus.Pending) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidRequestStatus, "cannot update request ID "
                        + requestId + " as it is not in Pending status");
            }

            // Update user permission request
            request.setRequestStatus(requestStatus);
            request.setReviewer(resolveUser(reviewerUserId, reviewerUserDomain));
            request.setReviewDate(ZonedDateTime.now());
            request.setReviewComment(reviewComment);
            request.setNotificationStatus(NotificationStatus.N);
            request.setNotificationRetries(0L);
            aclRepository.saveAccessRequest(request);

            // Create user permission if request was approved
            if (request.getRequestStatus() == RequestStatus.Approved) {
                UserPermission userPermission = new UserPermission();
                userPermission.setEntityId(request.getEntityId());
                userPermission.setUser(request.getUser());
                userPermission.setModulePermission(request.getModulePermission());
                aclRepository.saveAccess(userPermission);
            }

            return request;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public UserPermissionRequest cancelAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                     String reviewComment) throws ApplicationException {
        try {
            // Get request
            UserPermissionRequest request = aclRepository.retrieveAccessRequest(requestId);
            if (request == null) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidRequest, "no user permission request exists" +
                        " for request ID " + requestId);
            }

            // Only pending requests can be updated
            if (request.getRequestStatus() != RequestStatus.Pending) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidRequestStatus, "cannot update request ID " +
                        requestId + " as it is not in Pending status");
            }

            // Update user permission request
            request.setRequestStatus(RequestStatus.Cancelled);
            request.setReviewer(resolveUser(reviewerUserId, reviewerUserDomain));
            request.setReviewDate(ZonedDateTime.now());
            request.setReviewComment(reviewComment);
            aclRepository.saveAccessRequest(request);

            return request;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public UserPermissionRequest setNotificationStatus(Long requestId, NotificationStatus notificationStatus, Long notificationRetries) throws ApplicationException {

        try {
            // Get request
            UserPermissionRequest request = aclRepository.retrieveAccessRequest(requestId);
            if (request == null) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidRequest, "no user permission request exists" +
                        " for request ID " + requestId);
            }

            // Update user permission request
            request.setNotificationStatus(notificationStatus);
            request.setNotificationRetries(notificationRetries);
            aclRepository.saveAccessRequest(request);

            return request;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public UserPermission grantAccess(String userId, UserDomain userDomain, String entityId, Module module,
                                      EntityType entityType, PermissionType permissionType,
                                      String reviewerUserId, UserDomain reviewerUserDomain,
                                      String reviewComment) throws ApplicationException {
        try {
            // Check if permission already exists
            List<UserPermission> existingPermissions = aclRepository.retrieveAccess(userId, userDomain, entityId,
                    module, entityType, permissionType);
            if (CollectionUtils.isNotEmpty(existingPermissions)) {
                throw new ApplicationException(ApplicationErrorCode.E_PermissionAlreadyExists, "the specified user permission"
                        + " has already been granted");
            }

            // Get module permission
            ModulePermission modulePermission = aclRepository.retrieveModulePermission(module, entityType, permissionType);
            if (modulePermission == null) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidPermission, "no module permission exists" +
                        " for module " + module + " and entity type " + entityType + " and permission type " + permissionType);
            }

            // Get user, create if user does not exist
            User user = resolveUser(userId, userDomain);

            // Create user permission
            UserPermission userPermission = new UserPermission();
            userPermission.setEntityId(entityId);
            userPermission.setUser(user);
            userPermission.setModulePermission(modulePermission);
            userPermission = aclRepository.saveAccess(userPermission);

            // Create approved user permission request for history tracking
            UserPermissionRequest request = new UserPermissionRequest();
            request.setEntityId(entityId);
            request.setUser(user);
            request.setModulePermission(modulePermission);
            request.setRequestStatus(RequestStatus.Approved);
            request.setRequestAction(RequestAction.Grant);
            request.setRequester(user);
            request.setRequestDate(ZonedDateTime.now());
            request.setReviewer(resolveUser(reviewerUserId, reviewerUserDomain));
            request.setReviewDate(ZonedDateTime.now());
            request.setReviewComment(reviewComment);
            request.setNotificationStatus(NotificationStatus.N);
            request.setNotificationRetries(0L);
            request = aclRepository.saveAccessRequest(request);

            return userPermission;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void revokeAccess(String userId, UserDomain userDomain, String entityId, Module module,
                             EntityType entityType, PermissionType permissionType,
                             String reviewerUserId, UserDomain reviewerUserDomain,
                             String reviewComment) throws ApplicationException {
        try {
            // Get user permission
            List<UserPermission> userPermissions = aclRepository.retrieveAccess(userId, userDomain, entityId, module,
                    entityType, permissionType);
            if (userPermissions.size() != 1) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidPermission, "the specified user permission" +
                        "does not exist");
            }

            // Delete user permission
            aclRepository.deleteAccess(userPermissions.get(0));

            // Create approved user permission request for history tracking
            User user = resolveUser(userId, userDomain);
            UserPermissionRequest request = new UserPermissionRequest();
            request.setEntityId(entityId);
            request.setUser(user);
            request.setModulePermission(userPermissions.get(0).getModulePermission());
            request.setRequestStatus(RequestStatus.Approved);
            request.setRequestAction(RequestAction.Revoke);
            request.setRequester(user);
            request.setRequestDate(ZonedDateTime.now());
            request.setReviewer(resolveUser(reviewerUserId, reviewerUserDomain));
            request.setReviewDate(ZonedDateTime.now());
            request.setReviewComment(reviewComment);
            request.setNotificationStatus(NotificationStatus.N);
            request.setNotificationRetries(0L);
            request = aclRepository.saveAccessRequest(request);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void deleteForEntity(String entityId, Module module, EntityType entityType) throws ApplicationException {
        try {
            // Delete all user permissions for entity
            List<UserPermission> userPermissions = aclRepository.retrieveAccess(null, null, entityId, module,
                    entityType, null);
            for (UserPermission userPermission : userPermissions) {
                aclRepository.deleteAccess(userPermission);
            }

            // Delete all user permission requests (including history) for entity
            List<UserPermissionRequest> requests = aclRepository.retrieveAccessRequests(null, null, entityId, module,
                    entityType, null, null, null);
            for (UserPermissionRequest request : requests) {
                aclRepository.deleteAccessRequest(request);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    private User resolveUser(String userId, UserDomain userDomain) {
        User user = aclRepository.retrieveUser(userId, userDomain);
        if (user == null) {
            logger.info("creating ACL user for user ID " + userId + " and domain " + userDomain);
            user = new User();
            user.setUserId(userId);
            user.setUserDomain(userDomain);
            user = aclRepository.saveUser(user);
        }
        return user;
    }

}
