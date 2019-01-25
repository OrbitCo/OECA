package gov.epa.oeca.services.acl.interfaces.rest.v1;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.acl.*;
import gov.epa.oeca.common.infrastructure.acl.AclService;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/acl/v1")
@Api(
        value = "Access Control List API",
        consumes = MediaType.APPLICATION_JSON,
        produces = MediaType.APPLICATION_JSON
)
public class AclResource extends BaseResource {

    private static final Logger logger = LoggerFactory.getLogger(AclResource.class);

    @Autowired
    AclService aclService;

    @GET
    @Path("retrieveApplicablePermissions")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all permissions for the specified module and entity type.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public List<PermissionType> retrieveModulePermissions(@ApiParam(required = true) @QueryParam("module") Module module,
                                                          @ApiParam(required = true) @QueryParam("entityType") EntityType entityType) {
        try {
            return aclService.retrieveApplicablePermissions(module, entityType);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("retrieveUserPermissionTypes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves the user's permissions for the specified user, module, and entity.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public List<PermissionType> retrieveUserPermissionTypes(@ApiParam(required = true) @QueryParam("userId") String userId,
                                                            @ApiParam(required = true) @QueryParam("userDomain") UserDomain userDomain,
                                                            @ApiParam(required = true) @QueryParam("entityId") String entityId,
                                                            @ApiParam(required = true) @QueryParam("module") Module module,
                                                            @ApiParam(required = true) @QueryParam("entityType") EntityType entityType) {
        try {
            return aclService.retrieveUserPermissionTypes(userId, userDomain, entityId, module, entityType);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("retrieveUserPermissions")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves the user's permissions for the specified user, module, and entity.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public List<UserPermission> retrieveUserPermissions(@ApiParam @QueryParam("userId") String userId,
                                                        @ApiParam @QueryParam("userDomain") UserDomain userDomain,
                                                        @ApiParam @QueryParam("entityId") String entityId,
                                                        @ApiParam @QueryParam("module") Module module,
                                                        @ApiParam @QueryParam("entityType") EntityType entityType,
                                                        @ApiParam @QueryParam("permissionType") PermissionType permissionType) {
        try {
            return aclService.retrieveUserPermissions(userId, userDomain, entityId, module, entityType, permissionType);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("checkUserPermission")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns true if the specified permission is granted for the specified user, module, and entity. Returns false if it is not.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public Boolean checkUserPermission(@ApiParam(required = true) @QueryParam("userId") String userId,
                                       @ApiParam(required = true) @QueryParam("userDomain") UserDomain userDomain,
                                       @ApiParam(required = true) @QueryParam("entityId") String entityId,
                                       @ApiParam(required = true) @QueryParam("module") Module module,
                                       @ApiParam(required = true) @QueryParam("entityType") EntityType entityType,
                                       @ApiParam(required = true) @QueryParam("permissionType") PermissionType permissionType) {
        try {
            return aclService.checkUserPermission(userId, userDomain, entityId, module, entityType, permissionType);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("retrievePendingRequests")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves pending ACL requests for the specified user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public List<UserPermissionRequest> retrievePendingRequests(UserPermissionSearchCriteria criteria) {
        try {
            return aclService.retrievePendingRequests(criteria.getUserId(), criteria.getUserDomain(),
                    criteria.getEntityId(), criteria.getModule(), criteria.getEntityType(), criteria.getNotificationStatus());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("retrieveCompletedRequests")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves completed ACL requests for the specified user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public List<UserPermissionRequest> retrieveCompletedRequests(UserPermissionSearchCriteria criteria) {
        try {
            return aclService.retrieveCompletedRequests(criteria.getUserId(), criteria.getUserDomain(),
                    criteria.getEntityId(), criteria.getModule(), criteria.getEntityType(), criteria.getNotificationStatus());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("createAccessRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a ACL request with status Pending for the specified user, module, entity, and permission.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public UserPermissionRequest createAccessRequest(UserPermissionRequest request) {
        try {
            return aclService.createAccessRequest(request.getUser().getUserId(), request.getUser().getUserDomain(),
                    request.getEntityId(), request.getModulePermission().getModule(),
                    request.getModulePermission().getEntityType(), request.getModulePermission().getPermissionType());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("approveAccessRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Approves an ACL request on behalf of the specified reviewer.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public UserPermissionRequest approveAccessRequest(UserPermissionRequest request) {
        try {
            return aclService.approveAccessRequest(request.getId(), request.getReviewer().getUserId(),
                    request.getReviewer().getUserDomain(), request.getReviewComment());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("rejectAccessRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Rejects an ACL request on behalf of the specified reviewer.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public UserPermissionRequest rejectAccessRequest(UserPermissionRequest request) {
        try {
            return aclService.rejectAccessRequest(request.getId(), request.getReviewer().getUserId(),
                    request.getReviewer().getUserDomain(), request.getReviewComment());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("cancelAccessRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Cancels an ACL request on behalf of the specified reviewer.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public UserPermissionRequest cancelAccessRequest(UserPermissionRequest request) {
        try {
            return aclService.cancelAccessRequest(request.getId(), request.getReviewer().getUserId(),
                    request.getReviewer().getUserDomain(), request.getReviewComment());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("setNotificationStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Sets ACL request notification sent flag.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public UserPermissionRequest setNotificationSent(UserPermissionRequest request) {
        try {
            return aclService.setNotificationStatus(request.getId(), request.getNotificationStatus(), request.getNotificationRetries());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("grantAccess")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Immediately grants a specific permission to the specified user, module, and entity.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public UserPermission grantAccess(UserPermissionRequest request) {
        try {
            return aclService.grantAccess(request.getUser().getUserId(), request.getUser().getUserDomain(),
                    request.getEntityId(), request.getModulePermission().getModule(),
                    request.getModulePermission().getEntityType(), request.getModulePermission().getPermissionType(),
                    request.getReviewer().getUserId(), request.getReviewer().getUserDomain(),
                    request.getReviewComment());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("revokeAccess")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Immediately revokes a specific permission to the specified user, module, and entity.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public void revokeAccess(UserPermissionRequest request) {
        try {
            aclService.revokeAccess(request.getUser().getUserId(), request.getUser().getUserDomain(),
                    request.getEntityId(), request.getModulePermission().getModule(),
                    request.getModulePermission().getEntityType(), request.getModulePermission().getPermissionType(),
                    request.getReviewer().getUserId(), request.getReviewer().getUserDomain(),
                    request.getReviewComment());
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @DELETE
    @Path("deleteForEntity")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Deletes all permissions and requests (including history) for the specified module and entity.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token.", dataType = "string", paramType = "header")})
    public void deleteForEntity(@ApiParam(required = true) @QueryParam("entityId") String entityId,
                                       @ApiParam(required = true) @QueryParam("module") Module module,
                                       @ApiParam(required = true) @QueryParam("entityType") EntityType entityType) {
        try {
            aclService.deleteForEntity(entityId, module, entityType);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

}
