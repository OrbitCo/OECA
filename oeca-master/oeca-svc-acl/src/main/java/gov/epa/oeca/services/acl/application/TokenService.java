package gov.epa.oeca.services.acl.application;


import gov.epa.oeca.common.ApplicationException;

/**
 * @author dfladung
 */
public interface TokenService {

    String authenticate(TokenCredentials credentials) throws ApplicationException;
}
