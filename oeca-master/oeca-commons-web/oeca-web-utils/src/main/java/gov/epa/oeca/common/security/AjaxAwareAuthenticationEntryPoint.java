package gov.epa.oeca.common.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dfladung
 */
public class AjaxAwareAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    private String errorPage;

    public AjaxAwareAuthenticationEntryPoint(String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        if (AjaxAwareHelper.isAjaxRequest(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            response.sendRedirect(errorPage);
        }
    }
}
