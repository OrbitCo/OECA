package gov.epa.oeca.common.security.cdx;

import gov.epa.oeca.common.infrastructure.naas.securitytoken.SecurityTokenClient;
import gov.epa.oeca.common.security.jwt.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dfladung
 */
public class CdxHandoffPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {


    @Autowired
    JwtTokenUtil tokenUtil;
    @Autowired
    SecurityTokenClient securityTokenClient;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!StringUtils.isEmpty(securityTokenClient.extractTokenFromRequest((HttpServletRequest) request))
                && !StringUtils.isEmpty(securityTokenClient.extractIpFromRequest((HttpServletRequest) request))) {
            SecurityContextHolder.clearContext();
        }
        super.doFilter(request, response, chain);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        // overloading token as principal
        return securityTokenClient.extractTokenFromRequest(httpServletRequest);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        // overloading IP as credentials
        return securityTokenClient.extractIpFromRequest(httpServletRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, authResult);
    }
}
