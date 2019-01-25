package gov.epa.oeca.common.security.form;

import gov.epa.oeca.common.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Activate this in the applications that have sign-in using:
 * <http ...
 * <custom-filter position="FORM_LOGIN_FILTER" ref="oecaUsernamePasswordAuthenticationFilter">
 *
 * @author dfladung
 */
public class FormUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    JwtTokenUtil tokenUtil;

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        tokenUtil.insertTokenIntoCookie(tokenUtil.refreshToken(authResult), response);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
