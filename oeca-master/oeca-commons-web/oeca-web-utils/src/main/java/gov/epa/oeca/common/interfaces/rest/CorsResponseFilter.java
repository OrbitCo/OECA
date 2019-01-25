package gov.epa.oeca.common.interfaces.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * @author dfladung
 */
public class CorsResponseFilter implements ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(ContainerResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
//        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        headers.add("Access-Control-Expose-Headers", "Location");
        if (!StringUtils.isEmpty(containerRequestContext.getHeaderString("Access-Control-Request-Headers"))) {
            headers.add("Access-Control-Allow-Headers", containerRequestContext.getHeaderString("Access-Control-Request-Headers"));
        }
    }
}
