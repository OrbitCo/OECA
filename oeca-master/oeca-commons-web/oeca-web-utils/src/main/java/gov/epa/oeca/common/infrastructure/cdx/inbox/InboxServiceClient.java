package gov.epa.oeca.common.infrastructure.cdx.inbox;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.wsdl.register.inbox._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@Component
public class InboxServiceClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(InboxServiceClient.class);

    protected RegisterInboxService getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (RegisterInboxService) this.getClient(endpoint.toString(), RegisterInboxService.class, mtom, chunking);
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

    public void createInboxMessage(URL endpoint, String token, User user, String from, String subject, String message)
            throws ApplicationException {
        try {
            getClient(endpoint, false, true).createInboxMessage(token, user.getUserId(), from, subject, message);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public void createInboxMessageWithAttachments(URL endpoint, String token, User user, String from, String subject,
                                                  String message, String dataflow, InboxAttachments attachments)
            throws ApplicationException {
        try {
            getClient(endpoint, false, true).createInboxMessageWithAttachments(token, user.getUserId(), from,
                    subject, message, dataflow, attachments);
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
