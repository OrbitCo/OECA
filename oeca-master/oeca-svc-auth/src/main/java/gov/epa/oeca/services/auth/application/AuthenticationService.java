package gov.epa.oeca.services.auth.application;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.security.ApplicationUser;
import net.exchangenetwork.wsdl.register.auth._1.RegistrationUser;

/**
 * Service for making SOAP calls to CDX Authentication Services.
 *
 * @author smckay
 */
public interface AuthenticationService {
    /**
     * Authenticates a user with cdx and returns back the object cdx returns
     *
     * @param user user name and password to validate
     * @return {@link RegistrationUser} of the user logged
     * @throws ApplicationException
     */
    RegistrationUser authenticate(User user) throws ApplicationException;

    /**
     * Calls {{@link #authenticate(User)} to authenticate the user then generates a Handoff Token that can be passed to CDX to send the user to somewhere in CDX already logged in.
     *
     * @param user user name and password of the user to log in
     * @return token that can be passed to CDX
     */
    String generateHandoffToken(User user);

}
