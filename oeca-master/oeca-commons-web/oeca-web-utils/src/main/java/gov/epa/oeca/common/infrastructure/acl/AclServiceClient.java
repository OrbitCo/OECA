package gov.epa.oeca.common.infrastructure.acl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.acl.*;
import gov.epa.oeca.common.infrastructure.rest.BaseRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * NOTE: This class is reserved for potential future use.
 * Internal integration with other OECA applications should integrate AclService.
 * @see gov.epa.oeca.common.infrastructure.acl.AclService
 * External integration should use the REST services exposed by AclResource within oeca-svc-acl project.
 */
@Component
public class AclServiceClient extends BaseRestClient {

	private static final Logger logger = LoggerFactory.getLogger(AclServiceClient.class);
	private static final String tokenResource = "/api/token/v1";
	private static final String aclResource = "/api/acl/v1";

    protected String credential;

	public AclServiceClient() {
    	super(null, logger);
    }

	public AclServiceClient(String baseEndpointUri, String credential) {
	    super(baseEndpointUri, logger);
	    this.credential = credential;
    }

    public List<PermissionType> retrieveApplicablePermissions(Module module, EntityType entityType) throws ApplicationException {
        try {
            HttpEntity<?> entity = new HttpEntity(generateAuthorizationHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(baseEndpointUri + aclResource + "/retrieveApplicablePermissions")
                    .queryParam("module", module)
                    .queryParam("entityType", entityType);
            return template.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<PermissionType>>() {} ).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public List<PermissionType> retrieveUserPermissionTypes(String userId, UserDomain userDomain, String entityId, Module module,
                                                            EntityType entityType) throws ApplicationException {
        try {
            HttpEntity<?> entity = new HttpEntity(generateAuthorizationHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(baseEndpointUri + aclResource + "/retrieveUserPermissionTypes")
                    .queryParam("userId", userId)
                    .queryParam("userDomain", userDomain)
                    .queryParam("entityId", entityId)
                    .queryParam("module", module)
                    .queryParam("entityType", entityType);
            return template.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<PermissionType>>() {} ).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public List<UserPermission> retrieveUserPermissions(String userId, UserDomain userDomain, String entityId,
                                                        Module module, EntityType entityType, PermissionType permissionType) throws ApplicationException {
        try {
            HttpEntity<?> entity = new HttpEntity(generateAuthorizationHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(baseEndpointUri + aclResource + "/retrieveUserPermissions")
                    .queryParam("userId", userId)
                    .queryParam("userDomain", userDomain)
                    .queryParam("entityId", entityId)
                    .queryParam("module", module)
                    .queryParam("entityType", entityType)
                    .queryParam("permissionType", permissionType);
            return template.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<UserPermission>>() {} ).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public Boolean checkUserPermission(String userId, UserDomain userDomain, String entityId, Module module,
                                       EntityType entityType, PermissionType permissionType) throws ApplicationException {
        try {
            HttpEntity<?> entity = new HttpEntity(generateAuthorizationHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(baseEndpointUri + aclResource + "/checkUserPermission")
                    .queryParam("userId", userId)
                    .queryParam("userDomain", userDomain)
                    .queryParam("entityId", entityId)
                    .queryParam("module", module)
                    .queryParam("entityType", entityType)
                    .queryParam("permissionType", permissionType);
            return template.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                    Boolean.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public List<UserPermissionRequest> retrievePendingRequests(String userId, UserDomain userDomain, String entityId,
                                                               Module module, EntityType entityType,
                                                               NotificationStatus notificationStatus) throws ApplicationException {
        try {
            UserPermissionSearchCriteria criteria = new UserPermissionSearchCriteria();
            criteria.setUserId(userId);
            criteria.setUserDomain(userDomain);
            criteria.setEntityId(entityId);
            criteria.setModule(module);
            criteria.setEntityType(entityType);
            criteria.setNotificationStatus(notificationStatus);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(criteria), headers);

            return template.exchange(baseEndpointUri + aclResource + "/retrievePendingRequests", HttpMethod.POST, entity,
                    new ParameterizedTypeReference<List<UserPermissionRequest>>() {} ).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public List<UserPermissionRequest> retrieveCompletedRequests(String userId, UserDomain userDomain, String entityId,
                                                                 Module module, EntityType entityType,
                                                                 NotificationStatus notificationStatus) throws ApplicationException {
        try {
            UserPermissionSearchCriteria criteria = new UserPermissionSearchCriteria();
            criteria.setUserId(userId);
            criteria.setUserDomain(userDomain);
            criteria.setEntityId(entityId);
            criteria.setModule(module);
            criteria.setEntityType(entityType);
            criteria.setNotificationStatus(notificationStatus);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(criteria), headers);

            return template.exchange(baseEndpointUri + aclResource + "/retrieveCompletedRequests", HttpMethod.POST, entity,
                    new ParameterizedTypeReference<List<UserPermissionRequest>>() {} ).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public UserPermissionRequest createAccessRequest(String userId, UserDomain userDomain, String entityId, Module module,
                                                     EntityType entityType, PermissionType permissionType) throws ApplicationException {
        try {
            UserPermission userPermission = new UserPermission();
            userPermission.setUser(new User());
            userPermission.getUser().setUserId(userId);
            userPermission.getUser().setUserDomain(userDomain);
            userPermission.setEntityId(entityId);
            userPermission.setModulePermission(new ModulePermission());
            userPermission.getModulePermission().setModule(module);
            userPermission.getModulePermission().setEntityType(entityType);
            userPermission.getModulePermission().setPermissionType(permissionType);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermission), headers);

            return template.exchange(baseEndpointUri + aclResource + "/createAccessRequest", HttpMethod.POST, entity,
                    UserPermissionRequest.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public UserPermissionRequest approveAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                      String reviewComment) throws ApplicationException {
        try {
            UserPermissionRequest userPermissionRequest = new UserPermissionRequest();
            userPermissionRequest.setId(requestId);
            userPermissionRequest.setReviewer(new User());
            userPermissionRequest.getReviewer().setUserId(reviewerUserId);
            userPermissionRequest.getReviewer().setUserDomain(reviewerUserDomain);
            userPermissionRequest.setReviewComment(reviewComment);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermissionRequest), headers);

            return template.exchange(baseEndpointUri + aclResource + "/approveAccessRequest", HttpMethod.POST, entity,
                    UserPermissionRequest.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public UserPermissionRequest rejectAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                     String reviewComment) throws ApplicationException {
        try {
            UserPermissionRequest userPermissionRequest = new UserPermissionRequest();
            userPermissionRequest.setId(requestId);
            userPermissionRequest.setReviewer(new User());
            userPermissionRequest.getReviewer().setUserId(reviewerUserId);
            userPermissionRequest.getReviewer().setUserDomain(reviewerUserDomain);
            userPermissionRequest.setReviewComment(reviewComment);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermissionRequest), headers);

            return template.exchange(baseEndpointUri + aclResource + "/rejectAccessRequest", HttpMethod.POST, entity,
                    UserPermissionRequest.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public UserPermissionRequest cancelAccessRequest(Long requestId, String reviewerUserId, UserDomain reviewerUserDomain,
                                                     String reviewComment) throws ApplicationException {
        try {
            UserPermissionRequest userPermissionRequest = new UserPermissionRequest();
            userPermissionRequest.setId(requestId);
            userPermissionRequest.setReviewer(new User());
            userPermissionRequest.getReviewer().setUserId(reviewerUserId);
            userPermissionRequest.getReviewer().setUserDomain(reviewerUserDomain);
            userPermissionRequest.setReviewComment(reviewComment);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermissionRequest), headers);

            return template.exchange(baseEndpointUri + aclResource + "/cancelAccessRequest", HttpMethod.POST, entity,
                    UserPermissionRequest.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public UserPermissionRequest setNotificationStatus(Long requestId, NotificationStatus notificationStatus, Long notificationRetries) throws ApplicationException {
        try {
            UserPermissionRequest userPermissionRequest = new UserPermissionRequest();
            userPermissionRequest.setId(requestId);
            userPermissionRequest.setNotificationStatus(notificationStatus);
            userPermissionRequest.setNotificationRetries(notificationRetries);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermissionRequest), headers);

            return template.exchange(baseEndpointUri + aclResource + "/setNotificationStatus", HttpMethod.POST, entity,
                    UserPermissionRequest.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public UserPermission grantAccess(String userId, UserDomain userDomain, String entityId, Module module,
                                      EntityType entityType, PermissionType permissionType,
                                      String reviewerUserId, UserDomain reviewerUserDomain,
                                      String reviewComment) throws ApplicationException {
        try {
            UserPermissionRequest userPermissionRequest = new UserPermissionRequest();
            userPermissionRequest.setUser(new User());
            userPermissionRequest.getUser().setUserId(userId);
            userPermissionRequest.getUser().setUserDomain(userDomain);
            userPermissionRequest.setEntityId(entityId);
            userPermissionRequest.setModulePermission(new ModulePermission());
            userPermissionRequest.getModulePermission().setModule(module);
            userPermissionRequest.getModulePermission().setEntityType(entityType);
            userPermissionRequest.getModulePermission().setPermissionType(permissionType);
            userPermissionRequest.setReviewer(new User());
            userPermissionRequest.getReviewer().setUserId(reviewerUserId);
            userPermissionRequest.getReviewer().setUserDomain(reviewerUserDomain);
            userPermissionRequest.setReviewComment(reviewComment);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermissionRequest), headers);

            return template.exchange(baseEndpointUri + aclResource + "/grantAccess", HttpMethod.POST, entity,
                    UserPermission.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public void revokeAccess(String userId, UserDomain userDomain, String entityId, Module module,
                             EntityType entityType, PermissionType permissionType,
                             String reviewerUserId, UserDomain reviewerUserDomain,
                             String reviewComment) throws ApplicationException {
        try {
            UserPermissionRequest userPermissionRequest = new UserPermissionRequest();
            userPermissionRequest.setUser(new User());
            userPermissionRequest.getUser().setUserId(userId);
            userPermissionRequest.getUser().setUserDomain(userDomain);
            userPermissionRequest.setEntityId(entityId);
            userPermissionRequest.setModulePermission(new ModulePermission());
            userPermissionRequest.getModulePermission().setModule(module);
            userPermissionRequest.getModulePermission().setEntityType(entityType);
            userPermissionRequest.getModulePermission().setPermissionType(permissionType);
            userPermissionRequest.setReviewer(new User());
            userPermissionRequest.getReviewer().setUserId(reviewerUserId);
            userPermissionRequest.getReviewer().setUserDomain(reviewerUserDomain);
            userPermissionRequest.setReviewComment(reviewComment);

            // Request body will be JSON
            HttpHeaders headers = generateAuthorizationHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set content of request body for POST
            Gson gson = new Gson();
            HttpEntity<?> entity = new HttpEntity(gson.toJson(userPermissionRequest), headers);

            template.exchange(baseEndpointUri + aclResource + "/revokeAccess", HttpMethod.POST, entity, Object.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    public void deleteForEntity(String entityId, Module module, EntityType entityType) throws ApplicationException {
        try {
            HttpEntity<?> entity = new HttpEntity(generateAuthorizationHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(baseEndpointUri + aclResource + "/deleteForEntity")
                    .queryParam("entityId", entityId)
                    .queryParam("module", module)
                    .queryParam("entityType", entityType);
            template.exchange(builder.build().encode().toUri(), HttpMethod.DELETE, entity,
                    Boolean.class).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    private HttpHeaders generateAuthorizationHeaders() {
        // Request body will be JSON
	    HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set content of request body for POST
        JsonObject json = new JsonObject();
        json.addProperty("applicationId", "OECA_ACL");
        json.addProperty("applicationKey", credential);
        HttpEntity<?> entity = new HttpEntity(json.toString(), headers);

        // Call service to generate token
        String token = template.exchange(baseEndpointUri + tokenResource, HttpMethod.POST, entity,
                String.class).getBody();

        // Generate authorization headers for subsequent service call
        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

}