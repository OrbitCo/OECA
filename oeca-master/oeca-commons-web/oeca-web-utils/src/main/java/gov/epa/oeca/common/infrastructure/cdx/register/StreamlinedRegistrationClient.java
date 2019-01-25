package gov.epa.oeca.common.infrastructure.cdx.register;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.Esa;
import gov.epa.oeca.common.domain.registration.Role;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.wsdl.register.idp._1.IdentityProofingResultType;
import net.exchangenetwork.wsdl.register.idp._1.IdentityProofingUserType;
import net.exchangenetwork.wsdl.register.streamlined._1.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author dfladung
 */
@Component
public class StreamlinedRegistrationClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(StreamlinedRegistrationClient.class);

    protected StreamlinedRegistrationService getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (StreamlinedRegistrationService) this.getClient(endpoint.toString(), StreamlinedRegistrationService.class, mtom, chunking);
    }

    public String authenticate(URL endpoint, String operatorId, String operatorPassword) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).authenticate(operatorId, operatorPassword, domain, authMethod);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationDataflow> retrieveDataflowsByCategory(
            URL endpoint, String securityToken, String categoryAcronym) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveDataflowsByCategory(securityToken, categoryAcronym);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationRoleType> retrieveRolesByDataflowAcronym(URL endpoint, String securityToken, String dataflowAcronym)
            throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveRoleTypes(securityToken, dataflowAcronym);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationUserSuffix> retrieveSuffixes(URL endpoint, String securityToken) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllUserSuffixes(securityToken);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationUserTitle> retrieveTitles(URL endpoint, String securityToken) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllUserTitles(securityToken);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public Boolean isUserIdAvailable(URL endpoint, String securityToken, String userId) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).isUserIdAvailable(securityToken, userId);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public void validatePassword(URL endpoint, String securityToken, String userId, String password) throws ApplicationException {
        try {
            getClient(endpoint, false, true).validatePassword(securityToken, userId, password);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationQuestion> retrieveAllSecretQuestions(URL endpoint, String securityToken) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllSecretQuestions(securityToken);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationQuestion> retrieveAllElectronicSignatureQuestions(URL endpoint, String securityToken) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllElectronicSignatureQuestions(securityToken);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationOrganization> retrieveAllOrganizationsByCriteria(
            URL endpoint,
            String securityToken,
            RegistrationOrganization criteria) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllOrganizationsByCriteria(securityToken, criteria);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationState> retrieveAllRegistrationStates(
            URL endpoint,
            String securityToken) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllRegistrationStates(securityToken);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationCountry> retrieveAllRegistrationCountries(
            URL endpoint,
            String securityToken) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).retrieveAllRegistrationCountries(securityToken);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public RegistrationNewUserProfile createUser(URL endpoint,
                                                 String securityToken,
                                                 RegistrationNewUserProfile newUserProfile) {
        try {
            return getClient(endpoint, false, true).createNewUserProfile(securityToken, newUserProfile);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public IdentityProofingResultType validateIdentity(URL endpoint, String token, IdentityProofingUserType user) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).validateIdentity(token, user);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public Boolean doesUserRequireVerification(URL endpoint, String token, User user, Role role) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).doesUserRequireVerification(token, user.getUserId(), role.getUserRoleId());
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public void updateCdxEsaStatus(URL endpoint, String token, Esa esa) {
        try {
            getClient(endpoint, false, true).updateCdxEsaStatus(token, esa.getUser().getUserId(),
                    esa.getOrganization().getUserOrganizationId(), esa.getApprover(), esa.getStatus().toString(),
                    esa.getType().toString());
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public File retrieveEsaTemplate(URL endpoint, String token, RegistrationNewUserProfile profile) {
        try {
            byte[] result = getClient(endpoint, false, true).retrieveEsaTemplate(token, profile);
            File esa = File.createTempFile("ESA", ".html");
            FileUtils.writeByteArrayToFile(esa, result);
            return esa;
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public RegistrationUserSearchResult retrieveUsersByCriteria(URL endpoint, String token, RegistrationUserSearchCriteria criteria, Long firstRow, Long maxRows, String order) {
        try {
            return getClient(endpoint, false, true).retrieveUsersByCriteriaPaginated(token, criteria, firstRow, maxRows, order);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationRole> retrieveRolesByCriteria(URL endpoint, String token, RegistrationRoleSearchCriteria criteria) {
        try {
            return getClient(endpoint, false, true).retrieveRolesByCriteria(token, criteria);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public RegistrationRoleTypeDataElement retrieveRoleTypeDataElementByCode(URL endpoint, String token, Long roleId,
                                                                             String elementCode) {
        try {
            return getClient(endpoint, false, true).retrieveRoleTypeDataElementByCode(token, roleId, elementCode);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationOrganization> retrieveOrganizationsByRoleTypeStatusAndSubject(
            URL endpoint, String token, RegistrationUser user, RegistrationRoleType roleType, RegistrationRoleStatus roleStatus,
            String subject) {
        try {
            return getClient(endpoint, false, true).retrieveOrganizationsByRoleTypeStatusAndSubject(token, user, roleType, roleStatus, subject);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public RegistrationOrganization retrievePrimaryOrganization(URL endpoint, String token, RegistrationUser user) {
        try {
            return getClient(endpoint, false, true).retrievePrimaryOrganization(token, user);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public List<RegistrationRoleDataElement> retrieveRoleDataElements(URL endpoint, String token, Long userRoleId) {
        try {
            return getClient(endpoint, false, true).retrieveRoleDataElements(token, userRoleId);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    ApplicationException convertFault(RegisterException fault) {
        if (fault.getFaultInfo() == null) {
            return new ApplicationException(ApplicationErrorCode.E_RemoteServiceError, fault.getMessage());
        } else {
            ApplicationErrorCode code;
            try {
                code = ApplicationErrorCode.valueOf(fault.getFaultInfo().getErrorCode().value());
            } catch (Exception e) {
                logger.warn("Could not translate fault.");
                code = ApplicationErrorCode.E_RemoteServiceError;
            }
            return new ApplicationException(code, fault.getFaultInfo().getDescription());
        }
    }
}
