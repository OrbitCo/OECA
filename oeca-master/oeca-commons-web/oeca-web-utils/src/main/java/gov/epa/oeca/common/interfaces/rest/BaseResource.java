package gov.epa.oeca.common.interfaces.rest;

import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.io.FileCleaningTracker;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * @author dfladung
 */
public abstract class BaseResource {

    @Context
    protected HttpHeaders headers;

    @Context
    SecurityContext securityContext;

    protected FileCleaningTracker tracker;

    @PostConstruct
    void setup() {
        tracker = new FileCleaningTracker();
    }

    @PreDestroy
    void shutdown() {
        tracker.exitWhenFinished();
    }

    protected WebApplicationException translateException(ApplicationException e) {
        Response.ResponseBuilder response = Response.status(500);
        response.entity(String.format(
                "{\n\"code\" : \"%s\", \"message\" : \"%s\"\n}",
                e.getErrorCode(),
                e.getMessage()));
        return new WebApplicationException(response.build());
    }

}
