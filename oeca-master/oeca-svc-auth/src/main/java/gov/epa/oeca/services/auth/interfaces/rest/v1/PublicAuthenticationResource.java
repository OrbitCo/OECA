package gov.epa.oeca.services.auth.interfaces.rest.v1;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import gov.epa.oeca.services.auth.application.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.exchangenetwork.wsdl.register.auth._1.RegistrationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Component
@Path("/authentication/v1/public")
@Api(
        value = "Public Authentication API",
        consumes = "application/json",
        produces = "application/json"
)
public class PublicAuthenticationResource extends BaseResource {

    private static final Logger logger = LoggerFactory.getLogger(PublicAuthenticationResource.class);
    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    RegistrationHelper helper;

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Authenticates a user's credentails with CDX.")
    public RegistrationUser authenticate(@ApiParam(value = "The user's credentials to authenticate", required = true) User user) {
        try {
            return authenticationService.authenticate(user);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("authenticate/token/cdx")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Authenticates a user's credentials with CDX and returns a CDX Token to handoff the user off to CDX.  The url to log the into cdx will be set in the Location header.")
    public Response generateCdxToken(@ApiParam(value = "The user's credentials to authenticate", required = true) User user) {
        try {
            String token = authenticationService.generateHandoffToken(user);
            return Response.ok(new Token(token))
                    .location(
                            UriBuilder.fromUri(helper.getBaseCdxUrl() + "/SilentHandoff/NaasTokenSSO?ssoToken={token}")
                                    .build(token))
                    .build();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    //to return the token as json
    class Token {
        private String token;

        public Token(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
