package gov.epa.oeca.services.acl.interfaces.rest.v1;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import gov.epa.oeca.services.acl.application.TokenCredentials;
import gov.epa.oeca.services.acl.application.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author dfladung
 */
@Component
@Path("/token/v1")
@Api(value = "Access Control List Token API")
public class TokenResource extends BaseResource {

    private static final Logger logger = LoggerFactory.getLogger(TokenResource.class);

    @Context
    HttpServletResponse response;

    @Autowired
    private TokenService tokenService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Authenticate.")
    public String authenticate(@ApiParam(value = "User credentials.") TokenCredentials credentials) {
        try {
            return tokenService.authenticate(credentials);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }
}
