package gov.epa.oeca.services.acl.application;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.security.PasswordHasher;
import gov.epa.oeca.common.security.jwt.JwtTokenUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * @author dfladung
 */
@Component("tokenService")
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Resource(name = "oecaTokenConfiguration")
    Map<String, String> oecaTokenConfiguration;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String authenticate(TokenCredentials credentials) throws ApplicationException {
        try {
            // validate inputs
            Validate.notNull(credentials, "Credentials can't be empty.");
            Validate.notEmpty(credentials.getApplicationId(), "ID can't be empty.");
            Validate.notEmpty(credentials.getApplicationKey(), "Key can't be empty.");

            // authenticate the user
            String id = credentials.getApplicationId();
            String key = credentials.getApplicationKey();

            if (oecaTokenConfiguration.get(id) == null) {
                throw new ApplicationException(ApplicationErrorCode.E_Security, "Application ID not found: " + id);
            }
            if (!PasswordHasher.validatePassword(key, oecaTokenConfiguration.get(id))) {
                throw new ApplicationException(ApplicationErrorCode.E_Security, "Authentication Failed. Application Key not valid.");
            }

            // return the token
            return jwtTokenUtil.createTokenForUser(id, Collections.singletonList("ROLE_" + id.toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Validation, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
