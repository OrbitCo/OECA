package gov.epa.oeca.services.registration.application;

import freemarker.template.Configuration;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.dto.datatable.DataTableColumn;
import gov.epa.oeca.common.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.common.domain.dto.datatable.DataTableOrder;
import gov.epa.oeca.common.domain.registration.*;
import gov.epa.oeca.common.infrastructure.cdx.inbox.InboxServiceClient;
import gov.epa.oeca.common.infrastructure.cdx.register.Assembler;
import gov.epa.oeca.common.infrastructure.cdx.register.RegisterServiceClient;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.infrastructure.cdx.register.StreamlinedRegistrationClient;
import gov.epa.oeca.common.infrastructure.cdx.signature.SignatureServiceClient;
import net.exchangenetwork.wsdl.register.streamlined._1.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author dfladung
 */
@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    StreamlinedRegistrationClient streamlinedRegistrationClient;
    @Autowired
    SignatureServiceClient signatureServiceClient;
    @Autowired
    RegisterServiceClient registerServiceClient;
    @Autowired
    InboxServiceClient inboxServiceClient;
    @Autowired
    RegistrationHelper helper;
    @Autowired
    Assembler assembler;
    @Autowired
    ResourceLoader loader;
    @Resource(name = "additionalMailConfiguration")
    Map<String, String> additionalMailConfiguration;
    @Resource(name = "baseConfig")
    Map<String, String> baseConfig;
    @Autowired
    Configuration freemarkerConfiguration;

    @Override
    public List<Dataflow> retrieveDataflowsByCriteria(Map<String, String> criteria) throws ApplicationException {
        try {
            // validate that some criteria has been added
            if (criteria.values().isEmpty()) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, "Must specify dataflow criteria.");
            }

            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);

            // initialize the result
            List<RegistrationDataflow> dataflows = new ArrayList<>();
            // look up by category
            if (criteria.containsKey("categoryAcronym")) {
                dataflows = streamlinedRegistrationClient.retrieveDataflowsByCategory(
                        url,
                        token,
                        criteria.get("categoryAcronym"));
            }
            // translate the results
            return assembler.assembleDataflows(dataflows);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Role> retrieveRolesByCriteria(Map<String, String> criteria) throws ApplicationException {
        try {
            // validate that some criteria has been added
            if (criteria.values().isEmpty()) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, "Must specify role criteria.");
            }

            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);

            // initialize the result
            List<RegistrationRoleType> roles = new ArrayList<>();
            // look up by dataflow
            if (criteria.containsKey("dataflowAcronym")) {
                roles = streamlinedRegistrationClient.retrieveRolesByDataflowAcronym(url, token, criteria.get("dataflowAcronym"));
            }
            // translate the results
            return assembler.assembleRoles(roles);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("titles")
    public List<RegistrationUserTitle> retrieveTitles() throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveTitles(url, token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("suffixes")
    public List<RegistrationUserSuffix> retrieveSuffixes() throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveSuffixes(url, token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Boolean isUserIdAvailable(User user) throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.isUserIdAvailable(url, token, user.getUserId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Boolean validatePassword(User user) throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            streamlinedRegistrationClient.validatePassword(url, token, user.getUserId(), user.getPassword());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("secretQuestions")
    public List<RegistrationQuestion> retrieveAllSecretQuestions() {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveAllSecretQuestions(url, token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("electronicSignatureQuestions")
    public List<RegistrationQuestion> retrieveAllElectronicSignatureQuestions() {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveAllElectronicSignatureQuestions(url, token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Organization> searchOrganizations(Map<String, Object> criteria) throws ApplicationException {
        try {
            // validate that some criteria has been added
            if (criteria.values().isEmpty()) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify some organization criteria.");
            }
            String orgName = getValue("organizationName", String.class, criteria);
            String stateCode = getValue("state", String.class, criteria);
            if (orgName == null || orgName.replaceAll("\\s+", "").length() < 3) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify at least 3 characters in Organization Name.");
            }
            if (stateCode == null) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify Organization State.");
            }

            // add the criteria to the model object
            RegistrationOrganization org = new RegistrationOrganization();
            org.setOrganizationId(getValue("organizationId", Long.class, criteria));
            org.setOrganizationName(orgName);
            org.setMailingAddress1(getValue("address", String.class, criteria));
            org.setMailingAddress2(getValue("address2", String.class, criteria));
            org.setCity(getValue("city", String.class, criteria));
            org.setZip(getValue("zip", String.class, criteria));
            RegistrationCountry country = new RegistrationCountry();
            country.setCode(getValue("country", String.class, criteria));
            org.setCountry(country);
            RegistrationState state = new RegistrationState();
            state.setCode(stateCode);
            org.setState(state);

            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return assembler.assembleDomainOrganizations(
                    streamlinedRegistrationClient.retrieveAllOrganizationsByCriteria(url, token, org));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("states")
    public List<RegistrationState> retrieveAllStates() throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveAllRegistrationStates(url, token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("countries")
    public List<RegistrationCountry> retrieveAllCountries() throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveAllRegistrationCountries(url, token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public NewUserProfile createUser(NewUserProfile newUserProfile) throws ApplicationException {
        try {
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);

            RegistrationNewUserProfile assembledProfile = assembler.assembleNewUserProfile(newUserProfile);
            if (!StringUtils.isEmpty(newUserProfile.getUser().getJobTitle())) {
                RegistrationRoleTypeDataElement typeElement = streamlinedRegistrationClient.retrieveRoleTypeDataElementByCode(
                        url, token, newUserProfile.getRole().getCode(), "JOB_TITLE");

                RegistrationRoleDataElement element = new RegistrationRoleDataElement();
                element.setValue(newUserProfile.getUser().getJobTitle());
                element.setElement(typeElement);
                assembledProfile.getRoleElements().add(element);
            }

            RegistrationNewUserProfile result = streamlinedRegistrationClient.createUser(url, token, assembledProfile);
            NewUserProfile assembledResult = assembler.assembleNewUserProfile(result);
            assembledResult.getUser().setJobTitle(newUserProfile.getUser().getJobTitle());
            return assembledResult;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Boolean validateIdentity(IdentityProofingProfile profile) throws ApplicationException {
        try {
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            streamlinedRegistrationClient.validateIdentity(
                    url, token, assembler.assembleIdProofingUser(profile));
            User user = new User();
            user.setUserId(profile.getUserId());
            Role role = new Role();
            role.setUserRoleId(profile.getUserRoleId());
            if (streamlinedRegistrationClient.doesUserRequireVerification(url, token, user, role)) {
                throw new ApplicationException(ApplicationErrorCode.E_Verification, "Did not meet CROMERR verification minimum.");
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public File generateEsa(NewUserProfile newUserProfile) throws ApplicationException {
        try {
            URL registrationServiceUrl = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String registrationToken = getStreamlinedRegistrationToken(registrationServiceUrl);
            File esa = streamlinedRegistrationClient.retrieveEsaTemplate(registrationServiceUrl, registrationToken,
                    assembler.assembleNewUserProfile(newUserProfile));
            //send ESA to MyCDX inbox
            URL inboxServiceUrl = new URL(helper.getInboxServiceEndpoint());
            String inboxToken = getInboxToken(inboxServiceUrl);
            Map<String, Object> model = new HashMap<>();
            model.put("helpDeskEmail", additionalMailConfiguration.get("CdxHelpDeskEmail"));
            model.put("helpDeskPhone", additionalMailConfiguration.get("CdxHelpDeskPhone"));
            model.put("helpDeskHours", additionalMailConfiguration.get("CdxHelpDeskHours"));
            model.put("helpDeskInternationalPhone", additionalMailConfiguration.get("CdxHelpDeskInternationalPhone"));
            model.put("baseCdxUrl", baseConfig.get("baseCdxUrl"));
            String subject = mergeTemplate("notifications/esa/esa-subject.fm", model);
            String body = mergeTemplate("notifications/esa/esa-body.fm", model);
            inboxServiceClient.createInboxMessageWithAttachments(inboxServiceUrl, inboxToken,
                    newUserProfile.getUser(), "CDX Administrator", subject, body,
                    newUserProfile.getRole().getDataflow(), assembler.assembleInboxAttachments(Collections.singletonList(esa)));
            return esa;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    Boolean signEsa(NewUserProfile profile, Document document) throws ApplicationException {
        try {
            URL registrationServiceUrl = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String registrationToken = getStreamlinedRegistrationToken(registrationServiceUrl);
            if (streamlinedRegistrationClient.doesUserRequireVerification(registrationServiceUrl, registrationToken,
                    profile.getUser(), profile.getRole())) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "User can not sign ESA as they require further verification.");
            }

            // sign the document
            URL signatureServiceUrl = new URL(helper.getSignatureServiceEndpoint());
            String signatureToken = getSignatureToken(signatureServiceUrl);
            String activityId = signatureServiceClient.createActivity(signatureServiceUrl, signatureToken,
                    assembler.assembleSignatureUser(profile.getUser(), profile.getOrganization()), ESA_DATAFLOW);
            logger.info(String.format("Created activity for ESA signing: %s", activityId));
            signatureServiceClient.authenticateUser(signatureServiceUrl, signatureToken, activityId, profile.getUser());
            Answer answer = profile.getElectronicSignatureAnswers().get(0);
            signatureServiceClient.validateAnswer(signatureServiceUrl, signatureToken, activityId, profile.getUser(), answer);
            String documentId = signatureServiceClient.sign(signatureServiceUrl, signatureToken, activityId,
                    assembler.assembleSignatureDocument(document));
            logger.info(String.format("Signed ESA document: %s", documentId));

            // update ESA status
            Esa esa = new Esa();
            esa.setUser(profile.getUser());
            esa.setOrganization(profile.getOrganization());
            esa.setStatus(EsaStatus.Received);
            esa.setType(EsaType.Electronic);
            esa.setApprover(RegistrationService.ESA_APPROVER);
            streamlinedRegistrationClient.updateCdxEsaStatus(registrationServiceUrl, registrationToken, esa);
            logger.info(String.format("Updated ESA status for user: %s", profile.getUser().getUserId()));

            // update role status
            URL registerServiceUrl = new URL(helper.getRegisterServiceEndpoint());
            net.exchangenetwork.wsdl.register._1.RegistrationUser registerUser =
                    new net.exchangenetwork.wsdl.register._1.RegistrationUser();
            registerUser.setUserId(profile.getUser().getUserId());
            net.exchangenetwork.wsdl.register._1.RegistrationOrganization registerOrganization =
                    new net.exchangenetwork.wsdl.register._1.RegistrationOrganization();
            registerOrganization.setUserOrganizationId(profile.getOrganization().getUserOrganizationId());
            net.exchangenetwork.wsdl.register._1.RegistrationRole registerRole =
                    new net.exchangenetwork.wsdl.register._1.RegistrationRole();
            registerRole.setUserRoleId(profile.getRole().getUserRoleId());
            registerRole.setDataflow(profile.getRole().getDataflow());
            registerRole.setType(new net.exchangenetwork.wsdl.register._1.RegistrationRoleType());
            registerRole.getType().setCode(profile.getRole().getCode());
            registerServiceClient.approveRole(registerServiceUrl, getRegistrationToken(registerServiceUrl),
                    registerUser, registerOrganization, registerRole);
            logger.info(String.format("Updated role status to Active for user: %s and user role ID: %s",
                    profile.getUser().getUserId(), profile.getRole().getUserRoleId()));

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }


    @Override
    public Boolean signEsa(NewUserProfile profile) throws ApplicationException {
        try {
            URL registrationServiceUrl = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String registrationToken = getStreamlinedRegistrationToken(registrationServiceUrl);
            File tmpFile = streamlinedRegistrationClient.retrieveEsaTemplate(registrationServiceUrl, registrationToken,
                    assembler.assembleNewUserProfile(profile));
            Document esa = new Document();
            esa.setName("ESA.html");
            esa.setContent(tmpFile);
            return this.signEsa(profile, esa);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public RegistrationUserSearchResult searchUsers(Map<String, Object> criteria) throws ApplicationException {
        try {
            DataTableCriteria<Map<String, Object>> dtCriteria = new DataTableCriteria<>();
            dtCriteria.setCriteria(criteria);
            return searchUsers(dtCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public RegistrationUserSearchResult searchUsers(DataTableCriteria<Map<String, Object>> criteria) throws ApplicationException {
        try {
            Map<String, Object> userCriteria = criteria.getCriteria();
            // validate that some criteria has been added
            if (userCriteria.values().isEmpty()) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify some user criteria.");
            }
            for (Map.Entry<String, Object> e : userCriteria.entrySet()) {
                if (e.getValue() != null && String.class.isAssignableFrom(e.getValue().getClass()) && StringUtils.isBlank((String) e.getValue())) {
                    e.setValue(null);
                }
            }
            String lastName = getValue("lastName", String.class, userCriteria);
            String organizationName = getValue("organization", String.class, userCriteria);
            String organizationAddress = getValue("address", String.class, userCriteria);
            String email = getValue("email", String.class, userCriteria);
            if (lastName != null && lastName.replaceAll("\\s+", "").length() < 3) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify at least 3 characters in Last Name.");
            }
            if (organizationName != null && organizationName.replaceAll("\\s+", "").length() < 3) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify at least 3 characters in Organization.");
            }
            if (organizationAddress != null && organizationAddress.replaceAll("\\s+", "").length() < 3) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify at least 3 characters in Address.");
            }
            if (email != null && email.replaceAll("\\s+", "").length() < 3) {
                throw new ApplicationException(
                        ApplicationErrorCode.E_InvalidArgument,
                        "Must specify at least 3 characters in Email.");
            }

            // add the criteria to the model object
            RegistrationUserSearchCriteria model = new RegistrationUserSearchCriteria();
            model.setUserId(StringUtils.trim(getValue("userId", String.class, userCriteria)));
            model.setFirstName(StringUtils.trim(getValue("firstName", String.class, userCriteria)));
            model.setLastName(StringUtils.trim(lastName));
            model.setMiddleInitial(StringUtils.trim(getValue("middleInitial", String.class, userCriteria)));
            model.setOrganizationName(StringUtils.trim(organizationName));
            model.setOrganizationAddress(StringUtils.trim(organizationAddress));
            model.setEmail(StringUtils.trim(email));
            model.setDataflow(StringUtils.trim(getValue("dataflow", String.class, userCriteria)));
            RegistrationRoleType roleType = new RegistrationRoleType();
            roleType.setCode(getValue("roleId", Long.class, userCriteria));
            model.setRoleType(roleType);
            List<Object> roleIds = getValue("roleIds", List.class, userCriteria);
            if (roleIds != null) {
                for (Object roleId : roleIds) {
                    RegistrationRoleType rt = new RegistrationRoleType();
                    rt.setCode(asLong(roleId));
                    model.getRoleTypes().add(rt);
                }
            }
            RegistrationRoleStatus roleStatus = new RegistrationRoleStatus();
            roleStatus.setCode(StringUtils.trim(getValue("roleStatus", String.class, userCriteria)));
            model.setRoleStatus(roleStatus);
            model.setSubject(StringUtils.trim(getValue("subject", String.class, userCriteria)));

            //pagination config
            Long start = 0L;
            Long length = 100L;
            String order = buildUserSearchOrder(criteria);
            logger.info(String.format("Built order criteria: %s", order));
            if(criteria.getConfig() != null) {
                if(criteria.getConfig().getStart() != null) {
                    start = criteria.getConfig().getStart();
                }
                if(criteria.getConfig().getLength() != null) {
                    length = criteria.getConfig().getLength();
                }
            }
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            return streamlinedRegistrationClient.retrieveUsersByCriteria(url, token, model, start, length, order);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    private String buildUserSearchOrder(DataTableCriteria<Map<String, Object>> criteria) {
        if(criteria.getConfig() != null && criteria.getConfig().getOrder() != null) {
            List<String> orderCriterias = new ArrayList<>();
            String criteriaFormat = "%s %s";
            for (DataTableOrder order : criteria.getConfig().getOrder()) {
                DataTableColumn col = criteria.getConfig().getColumns().get(order.getColumn());
                String cdxCol = null;
                switch (col.getName()) {
                    case "user.userId":
                        orderCriterias.add(String.format(criteriaFormat, "userId", order.getDir().name()));
                        cdxCol = "";
                        break;
                    case "user.firstName":
                        orderCriterias.add(String.format(criteriaFormat, "firstName", order.getDir().name()));
                        break;
                    case "user.lastName":
                        orderCriterias.add(String.format(criteriaFormat, "lastName", order.getDir().name()));
                        break;
                    case "certifierName":
                        orderCriterias.add(String.format(criteriaFormat, "lastName", order.getDir().name()));
                        orderCriterias.add(String.format(criteriaFormat, "firstName", order.getDir().name()));
                        break;
                    case "org":
                        orderCriterias.add(String.format(criteriaFormat, "organizationName", order.getDir().name()));
                        break;
                    case "address":
                        orderCriterias.add(String.format(criteriaFormat, "organizationAddress", order.getDir().name()));
                        break;
                    case "email":
                        orderCriterias.add(String.format(criteriaFormat, "email", order.getDir().name()));
                        break;
                }
            }
            if (CollectionUtils.isNotEmpty(orderCriterias)) {
                return StringUtils.join(orderCriterias, ", ");
            }
        }
        return null;
    }

    @Override
    public String authenticateUser(String userId, String password) throws ApplicationException {
        try {
            URL url = new URL(helper.getRegisterServiceEndpoint());
            return registerServiceClient.authenticate(url, userId, password);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void changePassword(String userId, String newPassword, Boolean expire) throws ApplicationException {
        try {
            URL url = new URL(helper.getRegisterServiceEndpoint());
            String token = getRegistrationToken(url);
            registerServiceClient.changePassword(url, token, userId, newPassword, expire);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Role> retrieveUserRolesByCriteria(RegistrationRoleSearchCriteria criteria) throws ApplicationException {
        try {
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);

            // initialize the result
            List<RegistrationRole> roles = new ArrayList<>();
            // look up by email and dataflow category
            roles = streamlinedRegistrationClient.retrieveRolesByCriteria(url, token, criteria);

            // translate the results
            List<Role> results = new ArrayList<>();
            for (RegistrationRole role : roles) {
                results.add(assembler.assembleRole(role));
            }
            return results;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Organization> retrieveOrganizationsByUserRoleAndSubject(String userId, Long roleId,
                                                                        String subject) throws ApplicationException {
        try {
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            RegistrationUser user = new RegistrationUser();
            user.setUserId(userId);
            RegistrationRoleType roleType = new RegistrationRoleType();
            roleType.setCode(roleId);
            RegistrationRoleStatus roleStatus = new RegistrationRoleStatus();
            roleStatus.setCode("Active");
            return assembler.assembleDomainOrganizations(
                    streamlinedRegistrationClient.retrieveOrganizationsByRoleTypeStatusAndSubject(url, token, user, roleType, roleStatus, subject));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    <T> T getValue(String key, Class<T> type, Map<String, Object> criteria) {
        if (criteria.containsKey(key) && criteria.get(key) != null && type.isAssignableFrom(criteria.get(key).getClass())) {
            return (T) criteria.get(key);
        }
        return null;
    }
    Long asLong(Object o) {
        return o instanceof Number
                ? ((Number) o).longValue()
                : Long.parseLong(o.toString());
    }

    String getStreamlinedRegistrationToken(URL url) throws Exception {
        return streamlinedRegistrationClient.authenticate(
                url,
                helper.getStreamlinedRegisterOperatorUser(),
                helper.getStreamlinedRegisterOperatorPassword());
    }

    String getSignatureToken(URL url) throws Exception {
        return signatureServiceClient.authenticate(
                url,
                helper.getSignatureOperatorUser(),
                helper.getSignatureOperatorPassword());
    }

    String getRegistrationToken(URL url) throws Exception {
        return registerServiceClient.authenticate(
                url,
                helper.getRegisterOperatorUser(),
                helper.getRegisterOperatorPassword());
    }

    String getInboxToken(URL url) throws Exception {
        return inboxServiceClient.authenticate(
                url,
                helper.getInboxOperatorUser(),
                helper.getInboxOperatorPassword());
    }

    String mergeTemplate(String template, Map<String, Object> model) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration
                    .getTemplate(template), model);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Messaging, e.getMessage());
        }
    }

}
