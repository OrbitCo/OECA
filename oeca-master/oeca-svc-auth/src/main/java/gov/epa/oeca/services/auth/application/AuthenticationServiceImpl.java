package gov.epa.oeca.services.auth.application;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.cdx.auth.RegisterAuthClient;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.security.cdx.OecaUserHandoffService;
import net.exchangenetwork.wsdl.register.auth._1.RegistrationUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


    @Resource(name = "authRegisterAuthClient")
    RegisterAuthClient registerAuthClient;
    @Autowired
    RegistrationHelper helper;
    @Autowired
    OecaUserHandoffService oecaUserHandoffService;

    @Override
    public RegistrationUser authenticate(User user) throws ApplicationException {
        try {
            if (StringUtils.isEmpty(user.getUserId()) || StringUtils.isEmpty(user.getPassword())) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, "Must provide user id and password");
            }
            URL url = new URL(helper.getAuthServiceEndpoint());
            return registerAuthClient.authenticate(url, user.getUserId(), user.getPassword());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public String generateHandoffToken(User user) {
        authenticate(user);
        return oecaUserHandoffService.generateHandoffToken(user.getUserId(), null);
    }

}
