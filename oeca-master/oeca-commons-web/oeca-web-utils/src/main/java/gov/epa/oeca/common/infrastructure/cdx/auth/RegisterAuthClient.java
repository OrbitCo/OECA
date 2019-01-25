package gov.epa.oeca.common.infrastructure.cdx.auth;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.wsdl.register.auth._1.RegisterAuthException;
import net.exchangenetwork.wsdl.register.auth._1.RegisterAuthService;
import net.exchangenetwork.wsdl.register.auth._1.RegistrationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component("authRegisterAuthClient")
public class RegisterAuthClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(RegisterAuthClient.class);

    protected RegisterAuthService getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (RegisterAuthService) this.getClient(endpoint.toString(), RegisterAuthService.class, mtom, chunking);
    }

    public RegistrationUser authenticate(URL endpoint, String userId, String password) {
        try {
            return getClient(endpoint, false, true).authenticate(userId, password);
        } catch (RegisterAuthException fault) {
            throw this.handleException(convertFault(fault), logger);
        }
    }

    ApplicationException convertFault(RegisterAuthException fault) {
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
