package gov.epa.oeca.common.security;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dfladung
 */
public class AjaxAwareHelper {

    private static final String AJAX_REQUEST_HEADER = "X-Requested-With";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader(AJAX_REQUEST_HEADER) != null &&
                request.getHeader(AJAX_REQUEST_HEADER).equalsIgnoreCase(XML_HTTP_REQUEST);
    }

}
