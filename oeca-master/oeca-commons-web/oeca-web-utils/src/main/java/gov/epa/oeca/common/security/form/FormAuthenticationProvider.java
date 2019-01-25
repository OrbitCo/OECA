package gov.epa.oeca.common.security.form;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.cdx.auth.RegisterAuthClient;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.security.ApplicationUser;
import net.exchangenetwork.wsdl.register.auth._1.RegistrationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;

/**
 * @author dfladung
 */
@Component
public class FormAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationProvider.class);

    @Autowired
    RegisterAuthClient registerAuthClient;
    @Autowired
    RegistrationHelper registrationHelper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            URL endpoint = new URL(registrationHelper.getAuthServiceEndpoint());
            RegistrationUser ru = registerAuthClient.authenticate(endpoint, username, password);
            ApplicationUser user = new ApplicationUser(username, password, getRoles());
            BeanUtils.copyProperties(ru, user);
            decorateUser(user);
            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }

    protected Collection<SimpleGrantedAuthority> getRoles() {
        // add the most basic role by default
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_OECA_USER"));
    }

    protected void decorateUser(ApplicationUser user) {
        // do nothing by default, but allow specific web apps to add more information
    }

}
