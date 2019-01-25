package gov.epa.oeca.services.registration.application;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.dto.datatable.DataTableConfig;
import gov.epa.oeca.common.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.common.domain.registration.*;
import net.exchangenetwork.wsdl.register.streamlined._1.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-svc-registration-test.xml"})
public class RegistrationServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceTest.class);

    @Autowired
    RegistrationService registrationService;
    @Autowired
    ResourceLoader loader;

    File getFile(String file) throws Exception {
        return loader.getResource(file).getFile();
    }

    User getUser() {
        User user = new User();
        user.setUserId("JUNIT_" + UUID.randomUUID().toString());
        user.setFirstName("Jay");
        user.setLastName("Unit");
        user.setTitle("Mr");
        user.setGovernmentPartner(false);
        user.setJobTitle("Unit Tester");
        user.setPassword("Welcome1a");
        return user;
    }

    Organization getOrg() {
        Organization org = new Organization();
        org.setOrganizationName("JUNIT Org");
        org.setMailingAddress1("12601 Fair Lakes Circle");
        org.setCity("Fairfax");
        org.setStateCode("VA");
        org.setZip("22033");
        org.setCountryCode("US");
        org.setPhone("7032276000");
        org.setEmail("david.fladung@cgifederal.com");
        org.setPrimaryOrg(true);
        return org;
    }

    Role getRole() {
        Role role = new Role();
        role.setDataflow("NDMR-BAHDAR");
        role.setCode(113310L);
        return role;
    }

    List<Answer> getAnswers(Boolean cromerr, Boolean uniqueAnswer) {
        List<Answer> answers = new ArrayList<>();
        List<RegistrationQuestion> questions = (cromerr) ?
                registrationService.retrieveAllElectronicSignatureQuestions() :
                registrationService.retrieveAllSecretQuestions();
        int max = (cromerr) ? 5 : 3;
        for (int i = 0; i < max; i++) {
            Answer answer = new Answer();
            Question q = new Question();
            BeanUtils.copyProperties(questions.get(i), q);
            answer.setQuestion(q);
            answer.setAnswer((uniqueAnswer) ? "test " + answer.getQuestion().getId() : "test");
            answers.add(answer);
        }
        return answers;
    }

    @Test
    public void testRetrieveDataflowsByCategory() throws Exception {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("categoryAcronym", "NetDMR");
            List<Dataflow> dataflows = registrationService.retrieveDataflowsByCriteria(criteria);
            assertTrue(dataflows.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveDataflowsByCategory_invalidCategory() throws Exception {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("categoryAcronym", "bogus");
            registrationService.retrieveDataflowsByCriteria(criteria);
            fail("Should not have retrieved dataflows with bogus category.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_InvalidArgument, e.getErrorCode());
            assertEquals("Dataflow category does not exist.", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveRolesByDataflow() throws Exception {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("dataflowAcronym", "NDMR-BAHDAR");
            List<Role> roles = registrationService.retrieveRolesByCriteria(criteria);
            assertTrue(roles.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveRolesByDataflow_invalidDataflow() throws Exception {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("dataflowAcronym", "bogus");
            registrationService.retrieveRolesByCriteria(criteria);
            fail("Should not have retrieved roles with bogus dataflow.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_InvalidArgument, e.getErrorCode());
            assertEquals("dataflow does not exist.", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveSuffixes() throws Exception {
        try {
            List<RegistrationUserSuffix> suffixes = registrationService.retrieveSuffixes();
            assertTrue(suffixes.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveTitles() throws Exception {
        try {
            List<RegistrationUserTitle> titles = registrationService.retrieveTitles();
            assertTrue(titles.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testIsUserAvailable() throws Exception {
        try {
            User user = new User();
            user.setUserId("DFLADUNGAVAIL");
            assertTrue(registrationService.isUserIdAvailable(user));
            user.setUserId("DFLADUNG");
            assertFalse(registrationService.isUserIdAvailable(user));
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }


    @Test
    public void testValidatePassword() throws Exception {
        try {
            User user = new User();
            user.setUserId("DFLADUNG");
            user.setPassword("Welcome1");
            registrationService.validatePassword(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testValidatePassword_invalidUseOfUserName() throws Exception {
        try {
            User user = new User();
            user.setUserId("DFLADUNG");
            user.setPassword("DFLADUNG1");
            registrationService.validatePassword(user);
            fail("Should not have validated password that contains user ID.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_WeakPassword, e.getErrorCode());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveAllSecretQuestions() throws Exception {
        try {
            List<RegistrationQuestion> secretQuestions = registrationService.retrieveAllSecretQuestions();
            assertTrue(secretQuestions.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveAllElectronicSignatureQuestions() throws Exception {
        try {
            List<RegistrationQuestion> secretQuestions = registrationService.retrieveAllElectronicSignatureQuestions();
            assertTrue(secretQuestions.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveAllStates() throws Exception {
        try {
            List<RegistrationState> states = registrationService.retrieveAllStates();
            assertTrue(states.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveAllOrganizationsByCriteria() {
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("organizationName", "test");
            criteria.put("state", "VA");
            List<Organization> orgs = registrationService.searchOrganizations(criteria);
            assertTrue(orgs.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateUser() {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setOrganization(getOrg());
            profile.setRole(getRole());
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            assertNotNull(createdProfile);
            // validate user
            assertNotNull(createdProfile.getUser());
            assertEquals(createdProfile.getUser().getUserId(), profile.getUser().getUserId().toUpperCase());
            assertEquals(createdProfile.getUser().getFirstName(), profile.getUser().getFirstName());
            assertEquals(createdProfile.getUser().getLastName(), profile.getUser().getLastName());

            // validate org
            assertNotNull(createdProfile.getOrganization());
            assertEquals(createdProfile.getOrganization().getOrganizationName(), profile.getOrganization().getOrganizationName());
            assertEquals(createdProfile.getOrganization().getOrganizationName(), profile.getOrganization().getOrganizationName());
            assertEquals(createdProfile.getOrganization().getCity(), profile.getOrganization().getCity());
            assertEquals(createdProfile.getOrganization().getStateCode(), profile.getOrganization().getStateCode());
            assertEquals(createdProfile.getOrganization().getZip(), profile.getOrganization().getZip());
            assertNotNull(createdProfile.getOrganization().getOrganizationId());
            assertTrue(createdProfile.getOrganization().getOrganizationId() > 0);
            assertNotNull(createdProfile.getOrganization().getUserOrganizationId());
            assertTrue(createdProfile.getOrganization().getUserOrganizationId() > 0);

            // validate role
            assertNotNull(createdProfile.getRole());
            assertEquals(createdProfile.getRole().getDataflow(), profile.getRole().getDataflow());
            assertEquals(createdProfile.getRole().getCode(), profile.getRole().getCode());
            assertNotNull(createdProfile.getRole().getUserRoleId());
            assertTrue(createdProfile.getRole().getUserRoleId() > 0);
            assertEquals("AwaitingElectronicSignatureAgreement", createdProfile.getRole().getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateUserWithExistingOrganization() {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);

            // validate org
            assertNotNull(createdProfile.getOrganization());
            assertEquals(createdProfile.getOrganization().getOrganizationId(), org.getOrganizationId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test public void testGenerateEsa() {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            File esa = registrationService.generateEsa(createdProfile);
            assertTrue(esa.exists());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateNonSignatureUserWithExistingOrganization() {
        try {
            NewUserProfile profile = new NewUserProfile();
            User user = getUser();
            user.setJobTitle(null);
            profile.setUser(user);
            Role role = getRole();
            role.setCode(112520L);
            profile.setRole(role);
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);

            // validate org
            assertNotNull(createdProfile.getOrganization());
            assertEquals(createdProfile.getOrganization().getOrganizationId(), org.getOrganizationId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testValidateIdentity_doesNotMeet() {
        try {
            IdentityProofingProfile profile = new IdentityProofingProfile();
            profile.setUserId("DFLADUNG2");
            profile.setFirstName("David");
            profile.setLastName("Fladung");
            profile.setUserRoleId(84613L);

            profile.setMailingAddress1("1428 Elm Street");
            profile.setCity("Springwood");
            profile.setState("OH");
            profile.setZip("45501");

            profile.setPhone("7032276000");
            profile.setSSNLast4("9999");
            profile.setDateOfBirth(LocalDate.now().withYear(1979).withMonth(2).withDayOfMonth(27));

            registrationService.validateIdentity(profile);

            fail("Should not have reached this point with invalid identity information");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_Verification, e.getErrorCode());
            assertEquals("Did not meet CROMERR verification minimum.", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testValidateIdentity() {
        try {
            IdentityProofingProfile profile = new IdentityProofingProfile();

            // add your own personal information here you hackers!


            assertTrue(registrationService.validateIdentity(profile));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testSearchUsers() {
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("firstName", "dav");
            criteria.put("lastName", "fladu");
            criteria.put("organizationName", "cgi");
            criteria.put("organizationAddress", "12601");
            criteria.put("email", "fladu");
            criteria.put("dataflow", "NETEPACGP");
            criteria.put("roleId", 120420L);
            criteria.put("roleStatus", "Active");
            RegistrationUserSearchResult results = registrationService.searchUsers(criteria);
            assertTrue(results.getResults().size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testSearchUsersPaginate() {
        DataTableCriteria<Map<String, Object>> dtCriteria = new DataTableCriteria<>();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("dataflow", "NETEPACGP");
        criteria.put("roleId", 120420L);
        dtCriteria.setCriteria(criteria);
        DataTableConfig config = new DataTableConfig();
        config.setLength(1L);
        config.setStart(0L);
        dtCriteria.setConfig(config);
        RegistrationUserSearchResult results = registrationService.searchUsers(dtCriteria);
        assertTrue(CollectionUtils.isNotEmpty(results.getResults()));
        assertEquals(1L, results.getMaxRows().longValue());
        assertTrue(1 <= results.getTotalCount());
        //empty strings should not throw validation errors
        criteria.put("lastName", "");
        criteria.put("organization", "");
        criteria.put("address", " ");
        registrationService.searchUsers(dtCriteria);
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            registrationService.authenticateUser(profile.getUser().getUserId(), "Welcome1a");
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testAuthenticateUser_wrongPassword() throws Exception {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            registrationService.authenticateUser(profile.getUser().getUserId(), "Welcome1b");
            fail("Should not have authenticated using incorrect password.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_InvalidCredential, e.getErrorCode());
            assertTrue(e.getMessage().contains("The password is invalid."));
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testChangePassword() throws Exception {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            registrationService.changePassword(profile.getUser().getUserId(), "Welcome1b", false);
            registrationService.authenticateUser(profile.getUser().getUserId(), "Welcome1b");
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testChangePassword_wrongPassword() throws Exception {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            registrationService.changePassword(profile.getUser().getUserId(), "Welcome1b", false);
            registrationService.authenticateUser(profile.getUser().getUserId(), "Welcome1a");
            fail("Should not have authenticated using old password.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_InvalidCredential, e.getErrorCode());
            assertTrue(e.getMessage().contains("The password is invalid."));
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testChangePassword_doesNotMeetPasswordRequirements() throws Exception {
        try {
            NewUserProfile profile = new NewUserProfile();
            profile.setUser(getUser());
            profile.setRole(getRole());
            Organization org = new Organization();
            org.setOrganizationId(26769L);
            org.setPhone("7032276000");
            org.setEmail("david.fladung@cgifederal.com");
            profile.setOrganization(org);
            profile.setSecretAnswers(getAnswers(false, true));
            profile.setElectronicSignatureAnswers(getAnswers(true, true));
            NewUserProfile createdProfile = registrationService.createUser(profile);
            registrationService.changePassword(profile.getUser().getUserId(), "Short1", false);
            fail("Should not have changed to password that does not meet requirements.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_WeakPassword, e.getErrorCode());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveUserRolesByCriteria() throws Exception {
        try {
            RegistrationRoleSearchCriteria criteria = new RegistrationRoleSearchCriteria();
            criteria.getDataflowCategories().addAll(Arrays.asList("NeT", "NetDMR"));
			criteria.setEmail("kurt.tucker@cgifederal.com");
            List<Role> roles = registrationService.retrieveUserRolesByCriteria(criteria);
            assertTrue(roles.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveOrganizationsByUserRoleAndSubject() throws Exception {
        try {
            List<Organization> orgs = registrationService.retrieveOrganizationsByUserRoleAndSubject(
                    "KTUCKER1A", 11900L, null);
            assertTrue(orgs.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

}
