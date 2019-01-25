package gov.epa.oeca.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dfladung
 */
public class AjaxAwareAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(AjaxAwareAccessDeniedHandler.class);

    private String errorPage;

    public AjaxAwareAccessDeniedHandler(String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        logger.error(e.getMessage());
        if (AjaxAwareHelper.isAjaxRequest(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        } else {
            response.sendRedirect(errorPage);
        }
    }
}
