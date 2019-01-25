package gov.epa.oeca.common.security.cdx;

import gov.epa.oeca.common.security.ApplicationUser;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
public interface OecaUserHandoffService {

    String generateHandoffToken(String userName, ApplicationUser user);
}
