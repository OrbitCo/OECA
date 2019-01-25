package gov.epa.oeca.services.registration.interfaces.rest.v1;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.common.domain.registration.*;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import gov.epa.oeca.services.registration.application.RegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.exchangenetwork.wsdl.register.streamlined._1.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaPrintableArea;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dfladung
 */
@Component
@Path("/registration/v1")
@Api(
        value = "Registration Dataflow API"
)
public class RegistrationResource extends BaseResource {


    private static final Logger logger = LoggerFactory.getLogger(RegistrationResource.class);

    @Autowired
    RegistrationService registrationService;

    @GET
    @Path("/dataflow")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves list of dataflows for the specified criteria.")
    public List<Dataflow> retrieveDataflows(
            @ApiParam(value = "The dataflow category to search by (e.g. NetDMR).", required = false)
            @QueryParam("categoryAcronym") String categoryAcronym) {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("categoryAcronym", categoryAcronym);
            return registrationService.retrieveDataflowsByCriteria(criteria);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/role")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves list of roles for the specified dataflow.")
    public List<Role> retrieveRoles(
            @ApiParam(value = "The dataflow acronym to search by.", required = false)
            @QueryParam("dataflowAcronym") String dataflowAcronym) {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("dataflowAcronym", dataflowAcronym);
            return registrationService.retrieveRolesByCriteria(criteria);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/reference/title")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves list of titles.")
    public List<RegistrationUserTitle> retrieveTitles() {
        try {
            return registrationService.retrieveTitles();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/reference/suffix")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves list of suffixes.")
    public List<RegistrationUserSuffix> retrieveSuffixes() {
        try {
            return registrationService.retrieveSuffixes();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/reference/secretquestion")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves all elegible secret questions.")
    public List<RegistrationQuestion> retrieveAllSecretQuestions() {
        try {
            return registrationService.retrieveAllSecretQuestions();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/reference/cromerrquestion")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves all elegible electronic signature (CROMERR) questions.")
    public List<RegistrationQuestion> retrieveAllElectronicSignatureQuestions() {
        try {
            return registrationService.retrieveAllElectronicSignatureQuestions();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }


    @GET
    @Path("/reference/state")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves all states.")
    public List<RegistrationState> retrieveAllStates() {
        try {
            return registrationService.retrieveAllStates();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/reference/country")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves all countries.")
    public List<RegistrationCountry> retrieveAllCountries() {
        try {
            return registrationService.retrieveAllCountries();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/organization")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Search organizations by optional criteria")
    public List<Organization> searchOrganizations(
            @ApiParam(value = "The identifier of the organization.", required = false)
            @QueryParam("organizationId") Long organizationId,
            @ApiParam(value = "The name of the organization.", required = false)
            @QueryParam("organizationName") String organizationName,
            @ApiParam(value = "The address of the organization.", required = false)
            @QueryParam("address") String address,
            @ApiParam(value = "The secondary address of the organization.", required = false)
            @QueryParam("address2") String address2,
            @ApiParam(value = "The country of the organization.", required = false)
            @QueryParam("country") String country,
            @ApiParam(value = "The city of the organization.", required = false)
            @QueryParam("city") String city,
            @ApiParam(value = "The state abbreviation of the organization.", required = false)
            @QueryParam("state") String state,
            @ApiParam(value = "The zip code of the organization.", required = false)
            @QueryParam("zip") String zip) {
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("organizationId", organizationId);
            criteria.put("organizationName", organizationName);
            criteria.put("address", address);
            criteria.put("address2", address2);
            criteria.put("country", country);
            criteria.put("city", city);
            criteria.put("state", state);
            criteria.put("zip", zip);
            return registrationService.searchOrganizations(criteria);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/user/validation/id")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Checks to see if a user ID is available.")
    public Boolean isUserIdAvailable(User user) {
        try {
            return registrationService.isUserIdAvailable(user);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/user/validation/password")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Checks to see if a password is valid.")
    public void validatePassword(User user) {
        try {
            registrationService.validatePassword(user);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/user")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Creates a new user.")
    public NewUserProfile createUser(NewUserProfile profile) {
        try {
            return registrationService.createUser(profile);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/identity")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Validate a user's identity.")
    public Boolean validateIdentity(IdentityProofingProfile profile) {
        try {
            return registrationService.validateIdentity(profile);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/esa/generate")
    @Consumes("application/json")
    @Produces("text/html")
    @ApiOperation(value = "Sign an ESA.")
    public Response generateEsa(NewUserProfile profile) {
        try {
            File esa = registrationService.generateEsa(profile);
            return Response.ok(esa, MediaType.TEXT_HTML)
                    .header("Content-Disposition", "inline; filename=\"" + esa.getName() + "\"") //optional
                    .build();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }


    @POST
    @Path("/esa")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Sign an ESA.")
    public Boolean signEsa(NewUserProfile profile) {
        try {
            return registrationService.signEsa(profile);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/user/search")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Search users by optional criteria")
    public RegistrationUserSearchResult searchUsers(
            @ApiParam(value = "The user ID.", required = false)
            @QueryParam("userId") String userId,
            @ApiParam(value = "The user's first name.", required = false)
            @QueryParam("firstName") String firstName,
            @ApiParam(value = "The user's last name.", required = false)
            @QueryParam("lastName") String lastName,
            @ApiParam(value = "The user's middle initial.", required = false)
            @QueryParam("middleInitial") String middleInitial,
            @ApiParam(value = "The organization name.", required = false)
            @QueryParam("organization") String organization,
            @ApiParam(value = "The organization address, city, state abbreviation, or zip code.", required = false)
            @QueryParam("address") String address,
            @ApiParam(value = "The user's email address.", required = false)
            @QueryParam("email") String email,
            @ApiParam(value = "The dataflow acronym.", required = false)
            @QueryParam("dataflow") String dataflow,
            @ApiParam(value = "The role ID (role type code) (e.g. 120410).", required = false)
            @QueryParam("roleId") Long roleId,
            @ApiParam(value = "The list of role ID (role type code) (e.g. 120410).", required = false)
            @QueryParam("roleIds") List<Long> roleIds,
            @ApiParam(value = "The role status (e.g. Active).", required = false)
            @QueryParam("roleStatus") String roleStatus,
            @ApiParam(value = "The subject (aka Program ID or Client ID) of the role.", required = false)
            @QueryParam("subject") String subject) {
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("userId", checkIfNull(userId));
            criteria.put("firstName", checkIfNull(firstName));
            criteria.put("lastName", checkIfNull(lastName));
            criteria.put("middleInitial", checkIfNull(middleInitial));
            criteria.put("organization", checkIfNull(organization));
            criteria.put("address", checkIfNull(address));
            criteria.put("email", checkIfNull(email));
            criteria.put("dataflow", checkIfNull(dataflow));
            criteria.put("roleId", roleId);
            criteria.put("roleIds", roleIds);
            criteria.put("roleStatus", checkIfNull(roleStatus));
            criteria.put("subject", checkIfNull(subject));
            return registrationService.searchUsers(criteria);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/user/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("TODO")
    public RegistrationUserSearchResult searchUsers(DataTableCriteria<Map<String, Object>> criteria) {
        return registrationService.searchUsers(criteria);
    }

    private String checkIfNull(String value) {
        return (StringUtils.isNotEmpty(value) && !"null".equalsIgnoreCase(value)) ? value : null;
    }

}
