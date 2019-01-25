package gov.epa.oeca.common.infrastructure.cdx.register;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.wsdl.register.reviewer._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;


@Component
public class RegisterReviewerClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(RegisterReviewerClient.class);

    protected RegisterReviewerService getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (RegisterReviewerService) this.getClient(endpoint.toString(), RegisterReviewerService.class, mtom, chunking);
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

    public ReviewerDocumentType downloadDocumentsAsZip(URL endpoint, String securityToken, DocumentIds documentIds) throws ApplicationException {
        try {
            return getClient(endpoint, true, true).downloadDocumentsAsZip(securityToken, documentIds);
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
