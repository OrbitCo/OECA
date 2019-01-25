package gov.epa.oeca.common.infrastructure.cdx.signature;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.Answer;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.wsdl.register.sign._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * @author dfladung
 */
@Component
public class SignatureServiceClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SignatureServiceClient.class);

    protected RegisterSignService getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (RegisterSignService) this.getClient(endpoint.toString(), RegisterSignService.class, mtom, chunking);
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

    public String createActivity(URL endpoint, String token, SignatureUserType userType, String dataflow) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).createActivity(token, userType, dataflow);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public void authenticateUser(URL endpoint, String token, String activityId, User user) throws ApplicationException {
        try {
            getClient(endpoint, false, true).authenticateUser(token, activityId, user.getUserId(), user.getPassword(), new Properties());
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public Question getQuestion(URL endpoint, String token, String activityId, User user) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).getQuestion(token, activityId, user.getUserId());
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public Boolean validateAnswer(URL endpoint, String token, String activityId, User user, Answer answer) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).validateAnswer(token, activityId, user.getUserId(),
                    answer.getQuestion().getId().toString(), answer.getAnswer());
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public String sign(URL endpoint, String token, String activityId, SignatureDocumentType document) throws ApplicationException {
        try {
            return getClient(endpoint, true, true).sign(token, activityId, document);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public SignatureDocumentType downloadByDocumentId(URL endpoint, String token, String activityId, String documentId)
            throws ApplicationException {
        try {
            return getClient(endpoint, true, true).downloadByDocumentId(token, activityId, documentId);
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
