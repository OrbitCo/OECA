package gov.epa.oeca.common.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author dfladung
 */
@Service
public class JwtPreAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken)
            throws UsernameNotFoundException {
        return jwtTokenUtil.parseUserFromToken(
                preAuthenticatedAuthenticationToken.getCredentials().toString());
    }
}
