package gov.epa.oeca.common.infrastructure.cdx.register;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.wsdl.register._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * @author ktucker
 */
@Component
public class RegisterServiceClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceClient.class);

    protected RegisterService getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (RegisterService) this.getClient(endpoint.toString(), RegisterService.class, mtom, chunking);
    }

    public String authenticate(URL endpoint, String userId, String password) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).authenticate(userId, password, domain, authMethod);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public void changePassword(URL endpoint, String token, String userId, String newPassword, Boolean expire) throws ApplicationException {
        try {
            getClient(endpoint, false, true).changePassword(token, userId, newPassword, expire);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public void approveRole(URL endpoint, String token, RegistrationUser user, RegistrationOrganization organization,
                            RegistrationRole role) throws ApplicationException {
        try {
            getClient(endpoint, false, true).approveRole(token, user, organization, role);
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
