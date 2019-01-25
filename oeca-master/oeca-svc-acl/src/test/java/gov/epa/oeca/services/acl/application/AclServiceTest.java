package gov.epa.oeca.services.acl.application;

import gov.epa.oeca.common.domain.acl.*;
import gov.epa.oeca.common.infrastructure.acl.AclService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-svc-acl-test.xml"})
public class AclServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AclServiceTest.class);

    private static final String REVIEWER_USER_ID = "JUNIT_USER_REVIEWER";

    @Autowired
    AclService aclService;

    private String getUserId() {
        return "JUNIT_USER_" + UUID.randomUUID().toString();
    }

    private String getEntityId() {
        return "JUNIT_ENTITY_" + UUID.randomUUID().toString();
    }

    @Test
    public void testRetrieveModulePermissions() throws Exception {
        try {
            List<PermissionType> permissionTypes = aclService.retrieveApplicablePermissions(Module.GMG, EntityType.Operator);
            assertNotNull(permissionTypes);
            assertTrue("At least 1 permission type should have been retrieved.", permissionTypes.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testGrantAndRevokeAccess() throws Exception {
        try {
            String userId = getUserId();
            String entityId = getEntityId();

            // Grant access
            UserPermission userPermission = aclService.grantAccess(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Edit, REVIEWER_USER_ID, UserDomain.CDX, "Granted access");
            assertNotNull(userPermission);
            assertNotNull("Newly created user permission should have ID set.", userPermission.getId());

            // Check that access is allowed
            Boolean access = aclService.checkUserPermission(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Edit);
            assertEquals("Should have access after permission was granted.", true, access);

            // Revoke access
            aclService.revokeAccess(userId, UserDomain.CDX, entityId, Module.GMG, EntityType.Operator,
                    PermissionType.Edit, REVIEWER_USER_ID, UserDomain.CDX, "Revoked access");

            // Check that access is no longer allowed
            access = aclService.checkUserPermission(userId, UserDomain.CDX, getEntityId(),
                    Module.GMG, EntityType.Operator, PermissionType.Edit);
            assertEquals("Should no longer have access after permission was revoked.", false, access);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateAndApproveAccessRequest() throws Exception {
        try {
            String userId = getUserId();
            String entityId = getEntityId();

            // Create request
            UserPermissionRequest request = aclService.createAccessRequest(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertNotNull(request);
            assertNotNull("Newly created ACL request should have ID set.", request.getId());

            // Attempt to retrieve request
            List<UserPermissionRequest> requests = aclService.retrievePendingRequests(userId, UserDomain.CDX, null, null, null, null);
            assertNotNull(requests);
            assertEquals("Exactly 1 ACL request should have been retrieved.", 1, requests.size());
            assertEquals("ACL request should have Pending status set.", RequestStatus.Pending, request.getRequestStatus());
            assertEquals("ACL request should have Grant action set.", RequestAction.Grant, request.getRequestAction());

            // Check that access is not allowed yet since request is pending
            Boolean access = aclService.checkUserPermission(userId, UserDomain.CDX, getEntityId(),
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertEquals("Should not have access yet since ACL request is still Pending.", false, access);

            // Approve request
            request = aclService.approveAccessRequest(request.getId(), REVIEWER_USER_ID, UserDomain.CDX, "JUNIT approve comment");
            assertEquals("ACL request should now have Approved status set.", RequestStatus.Approved, request.getRequestStatus());
            assertNotNull("ACL request should have review date set.", request.getReviewDate());

            // Check that access is now allowed since request was approved
            access = aclService.checkUserPermission(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertEquals("Should now have access after ACL request was approved.", true, access);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateAndRejectAccessRequest() throws Exception {
        try {
            String userId = getUserId();
            String entityId = getEntityId();

            // Create request
            UserPermissionRequest request = aclService.createAccessRequest(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertNotNull(request);
            assertNotNull("Newly created ACL request should have ID set.", request.getId());

            // Attempt to retrieve request
            List<UserPermissionRequest> requests = aclService.retrievePendingRequests(userId, UserDomain.CDX, null, null, null, null);
            assertNotNull(requests);
            assertEquals("Exactly 1 ACL request should have been retrieved.", 1, requests.size());
            assertEquals("ACL request should have Pending status set.", RequestStatus.Pending, request.getRequestStatus());
            assertEquals("ACL request should have Grant action set.", RequestAction.Grant, request.getRequestAction());

            // Check that access is not allowed yet since request is pending
            Boolean access = aclService.checkUserPermission(userId, UserDomain.CDX, getEntityId(),
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertEquals("Should not have access yet since ACL request is still Pending.", false, access);

            // Reject request
            request = aclService.rejectAccessRequest(request.getId(), REVIEWER_USER_ID, UserDomain.CDX, "JUNIT approve comment");
            assertEquals("ACL request should now have Rejected status set.", RequestStatus.Rejected, request.getRequestStatus());
            assertNotNull("ACL request should have review date set.", request.getReviewDate());

            // Check that access is still not allowed since request was rejected
            access = aclService.checkUserPermission(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertEquals("Should still not have access after ACL request was rejected.", false, access);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateAndCancelAccessRequest() throws Exception {
        try {
            String userId = getUserId();
            String entityId = getEntityId();

            // Create request
            UserPermissionRequest request = aclService.createAccessRequest(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertNotNull(request);
            assertNotNull("Newly created ACL request should have ID set.", request.getId());

            // Attempt to retrieve request
            List<UserPermissionRequest> requests = aclService.retrievePendingRequests(userId, UserDomain.CDX, null, null, null, null);
            assertNotNull(requests);
            assertEquals("Exactly 1 ACL request should have been retrieved.", 1, requests.size());
            assertEquals("ACL request should have Pending status set.", RequestStatus.Pending, request.getRequestStatus());
            assertEquals("ACL request should have Grant action set.", RequestAction.Grant, request.getRequestAction());

            // Check that access is not allowed yet since request is pending
            Boolean access = aclService.checkUserPermission(userId, UserDomain.CDX, getEntityId(),
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertEquals("Should not have access yet since ACL request is still Pending.", false, access);

            // Cancel request
            request = aclService.cancelAccessRequest(request.getId(), REVIEWER_USER_ID, UserDomain.CDX, "JUNIT approve comment");
            assertEquals("ACL request should now have Cancelled status set.", RequestStatus.Cancelled, request.getRequestStatus());
            assertNotNull("ACL request should have review date set.", request.getReviewDate());

            // Check that access is still not allowed since request was cancelled
            access = aclService.checkUserPermission(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertEquals("Should still not have access after ACL request was cancelled.", false, access);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteForEntity() throws Exception {
        try {
            String userId = getUserId();
            String entityId = getEntityId();

            // Create request
            UserPermissionRequest request = aclService.createAccessRequest(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertNotNull(request);
            assertNotNull("Newly created ACL request should have ID set.", request.getId());

            // Attempt to retrieve request
            List<UserPermissionRequest> requests = aclService.retrievePendingRequests(userId, UserDomain.CDX, null, null, null, null);
            assertNotNull(requests);
            assertEquals("Exactly 1 ACL request should have been retrieved.", 1, requests.size());

            // Approve request
            request = aclService.approveAccessRequest(request.getId(), REVIEWER_USER_ID, UserDomain.CDX, "JUNIT approve comment");

            // Attempt to retrieve permission
            List<UserPermission> userPermissions = aclService.retrieveUserPermissions(userId, UserDomain.CDX, entityId, Module.GMG, EntityType.Operator, null);
            assertNotNull(userPermissions);
            assertEquals("Exactly 1 user permission should have been retrieved.", 1, userPermissions.size());

            // Delete everything for entity
            aclService.deleteForEntity(entityId, Module.GMG, EntityType.Operator);

            // No user permissions should exist now
            userPermissions = aclService.retrieveUserPermissions(userId, UserDomain.CDX, entityId, Module.GMG, EntityType.Operator, null);
            assertNotNull(userPermissions);
            assertEquals("0 user permissions should have been retrieved.", 0, userPermissions.size());

            // No requests should exist now
            requests = aclService.retrievePendingRequests(userId, UserDomain.CDX, null, null, null, null);
            assertNotNull(requests);
            assertEquals("0 ACL requests should have been retrieved.", 0, requests.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificationStatus() throws Exception {
        try {
            String userId = getUserId();
            String entityId = getEntityId();

            // Create request
            UserPermissionRequest request = aclService.createAccessRequest(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, PermissionType.Sign);
            assertNotNull(request);
            assertNotNull("Newly created ACL request should have ID set.", request.getId());
            assertEquals("Newly created ACL request should have notificationStatus equal to N.", NotificationStatus.N, request.getNotificationStatus());

            // Set request property
            aclService.setNotificationStatus(request.getId(), NotificationStatus.Y, 0L);

            // Retrieve request to check for updated property value
            List<UserPermissionRequest> requests = aclService.retrievePendingRequests(userId, UserDomain.CDX, entityId,
                    Module.GMG, EntityType.Operator, NotificationStatus.Y);

            assertNotNull(requests);
            assertEquals("Exactly 1 ACL request should have been retrieved.", 1, requests.size());
            assertEquals("ACL request should have notificationStatus property equal to Y.", NotificationStatus.Y, requests.get(0).getNotificationStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrievePendingsForAllEntities() {
        String manageUser = getUserId();
        String requestUser = getUserId();
        String entityId1 = getEntityId();
        String entityId2 = getEntityId();
        String entityId3 = getEntityId();
        String noAccessEntityId = getEntityId();

        //first grant the manage user manage permissions
        aclService.grantAccess(manageUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.Manage, REVIEWER_USER_ID, UserDomain.CDX, null);
        aclService.grantAccess(manageUser, UserDomain.CDX, entityId2, Module.GMG, EntityType.Operator,
                PermissionType.Manage, REVIEWER_USER_ID, UserDomain.CDX, null);
        aclService.grantAccess(manageUser, UserDomain.CDX, entityId3, Module.GMG, EntityType.Structure,
                PermissionType.Manage, REVIEWER_USER_ID, UserDomain.CDX, null);
        //verify access was granted
        assertTrue(aclService.checkUserPermission(manageUser, UserDomain.CDX, entityId1, Module.GMG,
                EntityType.Operator, PermissionType.Manage));
        assertTrue(aclService.checkUserPermission(manageUser, UserDomain.CDX, entityId2, Module.GMG,
                EntityType.Operator, PermissionType.Manage));
        assertTrue(aclService.checkUserPermission(manageUser, UserDomain.CDX, entityId3, Module.GMG,
                EntityType.Structure, PermissionType.Manage));

        //pull pending requests should be empty
        List<UserPermissionRequest> requests = aclService.retrievePendingRequestsForAllEntities(manageUser,
                UserDomain.CDX, Module.GMG, null, PermissionType.Manage);
        assertNotNull(requests);
        assertTrue(requests.isEmpty());

        //make some requests
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.View);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.Edit);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.Sign);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId3, Module.GMG, EntityType.Structure,
                PermissionType.View);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId2, Module.GMG, EntityType.Operator,
                PermissionType.View);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, noAccessEntityId, Module.GMG, EntityType.Operator,
                PermissionType.View);

        //pull back the request should only see 5
        requests = aclService.retrievePendingRequestsForAllEntities(manageUser, UserDomain.CDX, Module.GMG,null,
                PermissionType.Manage);
        assertNotNull(requests);
        assertEquals(5, requests.size());
        //filter by operator
        requests = aclService.retrievePendingRequestsForAllEntities(manageUser, UserDomain.CDX, Module.GMG,
                EntityType.Operator, PermissionType.Manage);
        assertNotNull(requests);
        assertEquals(4, requests.size());
        //filter by structure
        requests = aclService.retrievePendingRequestsForAllEntities(manageUser, UserDomain.CDX, Module.GMG,
                EntityType.Structure, PermissionType.Manage);
        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testRetrievePendingForAllUsersAdmin() {
        String requestUser = getUserId();
        String entityId1 = getEntityId();
        String entityId2 = getEntityId();
        String entityId3 = getEntityId();

        //pull pending requests should be empty
        List<UserPermissionRequest> requests = aclService.retrievePendingRequestsForAllEntities(Module.GMG, null);
        assertNotNull(requests);
        assertTrue(requests.isEmpty());

        //make some requests
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.View);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.Edit);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator,
                PermissionType.Sign);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId3, Module.GMG, EntityType.Structure,
                PermissionType.View);
        aclService.createAccessRequest(requestUser, UserDomain.CDX, entityId2, Module.GMG, EntityType.Operator,
                PermissionType.View);

        //pull back penidng requests should get 5
        requests = aclService.retrievePendingRequestsForAllEntities(Module.GMG, null);
        assertNotNull(requests);
        assertEquals(5, requests.size());
        //filter by operator
        requests = aclService.retrievePendingRequestsForAllEntities(Module.GMG, EntityType.Operator);
        assertNotNull(requests);
        assertEquals(4, requests.size());
        //filter by structure
        requests = aclService.retrievePendingRequestsForAllEntities(Module.GMG, EntityType.Structure);
        assertNotNull(requests);
        assertEquals(1, requests.size());
    }

    @Test
    public void testRetrievePermissionCountPerEntity() {
        String user1 = getUserId();
        String user2 = getUserId();
        String entityId1 = getEntityId();
        String entityId2 = getEntityId();
        aclService.grantAccess(user1, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator, PermissionType.View,
                REVIEWER_USER_ID, UserDomain.CDX, null);
        aclService.grantAccess(user1, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator, PermissionType.Edit,
                REVIEWER_USER_ID, UserDomain.CDX, null);
        aclService.grantAccess(user2, UserDomain.CDX, entityId1, Module.GMG, EntityType.Operator, PermissionType.View,
                REVIEWER_USER_ID, UserDomain.CDX, null);
        aclService.grantAccess(user2, UserDomain.CDX, entityId2, Module.GMG, EntityType.Operator, PermissionType.View,
                REVIEWER_USER_ID, UserDomain.CDX, null);
        List<String> entityIds = new ArrayList<>();
        entityIds.add(entityId1);
        entityIds.add(entityId2);
        Map<String, Long> results = aclService.retrievePermissionCountPerEntity(entityIds, PermissionType.View);
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(new Long(2), results.get(entityId1));
        assertEquals(new Long(1), results.get(entityId2));
        results = aclService.retrievePermissionCountPerEntity(entityIds, PermissionType.Edit);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(new Long(1), results.get(entityId1));
        assertEquals(null, results.get(entityId2));
        results = aclService.retrievePermissionCountPerEntity(entityIds, PermissionType.Sign);
        assertNotNull(results);
        assertEquals(0, results.size());
        assertEquals(null, results.get(entityId1));
        assertEquals(null, results.get(entityId2));

        //test over 1000 entity Ids
        entityIds = new ArrayList<>();
        for(int i = 0; i < 1100; ++i) {
            String entityId = getEntityId();
            aclService.grantAccess(user1, UserDomain.CDX, entityId, Module.GMG, EntityType.Operator, PermissionType.View,
                    REVIEWER_USER_ID, UserDomain.CDX, null);
            entityIds.add(entityId);
        }
        results = aclService.retrievePermissionCountPerEntity(entityIds, PermissionType.View);
        assertEquals(1100, results.size());
        for(Map.Entry<String, Long> entity : results.entrySet()) {
            assertEquals(new Long(1), entity.getValue());
        }
    }
}