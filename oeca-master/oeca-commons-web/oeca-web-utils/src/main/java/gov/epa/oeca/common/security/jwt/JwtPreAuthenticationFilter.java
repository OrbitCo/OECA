package gov.epa.oeca.common.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dfladung
 */
public class JwtPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String TOKEN_TRANSPORT_COOKIE = "Cookie";
    private static final String TOKEN_TRANSPORT_HEADER = "Header";
    private static final String TOKEN_PRINCIPAL = "OECA Application";
    ThreadLocal<String> tokenAccessMode = new ThreadLocal<>();

    @Autowired
    JwtTokenUtil tokenUtil;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return TOKEN_PRINCIPAL;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        // setting credential to token
        String token = tokenUtil.extractTokenFromCookie(httpServletRequest);
        if (StringUtils.isEmpty(token)) {
            token = tokenUtil.extractTokenFromHeader(httpServletRequest);
            if (!StringUtils.isEmpty(token)){
                tokenAccessMode.set(TOKEN_TRANSPORT_HEADER);
            }
        } else {
            tokenAccessMode.set(TOKEN_TRANSPORT_COOKIE);
        }
        return token;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
            throws IOException, ServletException {
        if (TOKEN_TRANSPORT_COOKIE.equals(tokenAccessMode.get())) {
            tokenUtil.insertTokenIntoCookie(tokenUtil.refreshToken(authResult), response);
        } else if (TOKEN_TRANSPORT_HEADER.equals(tokenAccessMode.get())){
            tokenUtil.insertTokenIntoHeader(tokenUtil.refreshToken(authResult), response);
        }
        tokenAccessMode.remove();
        super.successfulAuthentication(request, response, authResult);
    }


}
