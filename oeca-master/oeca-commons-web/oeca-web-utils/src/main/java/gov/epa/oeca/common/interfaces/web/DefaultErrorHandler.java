package gov.epa.oeca.common.interfaces.web;

import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author dfladung
 */
public class DefaultErrorHandler extends DefaultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultErrorHandler.class);
    private static final String AJAX_REQUEST_HEADER = "X-Requested-With";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader(AJAX_REQUEST_HEADER) != null &&
                request.getHeader(AJAX_REQUEST_HEADER).equalsIgnoreCase(XML_HTTP_REQUEST);
    }

    public Resolution handleGeneric(Throwable t, HttpServletRequest req, HttpServletResponse resp) {
        String errorId = UUID.randomUUID().toString();
        String errorMessage = t.getMessage();
        logger.error(String.format("[errorId = %s] errorMessage = %s ", errorId, t.getMessage()), t);
        if (isAjaxRequest(req)) {
            return new ErrorResolution(403, String.format("We encountered a technical problem. errorId=%s", errorId));
        } else {
            return new ForwardResolution("/action/error").addParameter("errorMessage", errorMessage)
                    .addParameter("errorId", errorId);
        }
    }
}
