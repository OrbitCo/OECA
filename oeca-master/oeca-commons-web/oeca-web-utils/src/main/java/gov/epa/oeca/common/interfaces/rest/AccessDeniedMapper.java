package gov.epa.oeca.common.interfaces.rest;

import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author dfladung
 */
@Provider
public class AccessDeniedMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException e) {
        return Response.status(403).build();
    }
}