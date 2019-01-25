package gov.epa.oeca.common.security;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * For microservice endpoints, the typical CSRF solution in Spring Security does not work because it is session-based.
 * So, this is an attempt at implementing
 * https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)_Prevention_Cheat_Sheet#Protecting_REST_Services:_Use_of_Custom_Request_Headers
 * as a way to mitigate CSRF attacks.
 *
 * @author dfladung
 */
public class CsrfFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CsrfFilter.class);
    private static final String csrfCustomHeader = "X-Requested-With";
    private static final List<String> csrfCustomHeaderValues = Collections.singletonList("XMLHttpRequest");
    private static final String originHeader = "Origin";
    private static final String refererHeader = "Referer";
    private static final String hostHeader = "Host";
    private static final String xForwardedHostHeader = "X-Forwarded-Host";
    private static final List<String> stateChangingVerbs = Arrays.asList("POST", "PUT", "DELETE", "PATCH");


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing CSRF filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String origin = req.getHeader(originHeader);
        String referer = req.getHeader(refererHeader);
        String host = req.getHeader(hostHeader);
        String xForwardedHost = req.getHeader(xForwardedHostHeader);
        if (stateChangingVerbs.contains(req.getMethod())) {
            try {
                logger.debug("Validating request");
            	String target = (StringUtils.isEmpty(origin)) ? referer : origin;
                Validate.notEmpty(target, "Origin or Referer headers are required.");
                Validate.isTrue(
                        StringUtils.contains(target, host) || StringUtils.contains(target, xForwardedHost),
                        "The source origin does not match the target origin.");

                String csrfHeaderValue = req.getHeader(csrfCustomHeader);
                Validate.notEmpty(csrfHeaderValue, "CSRF header is missing.");
                Validate.isTrue(csrfCustomHeaderValues.contains(csrfHeaderValue), "CSRF header has incorrect value.");
                logger.debug("request is valid");
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
                ((HttpServletResponse) servletResponse).sendError(403, e.getMessage());
                return;
            }
        }
        logger.debug("finished csrf filter forwarding chain.");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("Destroying CSRF filter");
    }
}
