package gov.epa.oeca.services.ref.interfaces.rest.v1;

import gov.epa.oeca.services.ref.application.ReferenceService;
import gov.epa.oeca.common.domain.ref.*;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/lookup/v1")
@Api(
        value = "Lookup Resource",
        consumes = MediaType.APPLICATION_JSON,
        produces = MediaType.APPLICATION_JSON
)
public class LookupResource extends BaseResource {
    @Autowired
    ReferenceService referenceService;

    @GET
    @Path("states")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all states")
    public List<State> retrieveStates() {
        return referenceService.retrieveStates();
    }

    @GET
    @Path("tribes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all tribes")
    public List<Tribe> retrieveTribes() {
        return referenceService.retrieveTribes();
    }

    @GET
    @Path("tribes/{stateCode}/{tribalLandName}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves tribe by land name and state code")
    public Tribe retrieveTribeByLandNameAndStateCode(@PathParam("tribalLandName") String tribalLandName, @PathParam("stateCode") String stateCode) {
        return referenceService.retrieveTribeByLandNameAndStateCode(tribalLandName, stateCode);
    }


    @GET
    @Path("biaTribes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all BIA tribes")
    public List<BiaTribe> retrieveBiaTribes() {
        return referenceService.retrieveBiaTribes();
    }

    @GET
    @Path("counties")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all counties")
    public List<County> retrieveCounties() {
        return referenceService.retrieveAllCounties();
    }

    @GET
    @Path("counties/{stateCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all counties for passed in state")
    public List<County> retrieveCounties(@PathParam("stateCode") String stateCode) {
        return referenceService.retrieveCounties(stateCode);
    }
    
    @GET
    @Path("chemicals")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all chemicals")
    public List<Chemical> retrieveChemicals() {
        return referenceService.retrieveChemicals();
    }

    @GET
    @Path("chemicals/{criteria}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Searches for chemicals.  At least 3 characters are required for the criteria to work.")
    public List<Chemical> retrieveChemicals(@PathParam("criteria") String criteria) {
        return referenceService.retrieveChemicals(criteria);
    }

    @GET
    @Path("pollutants/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Searches for pollutants.  At least 3 characters are required for the criteria to work.")
    public List<Pollutant> retrievePollutants(@QueryParam("criteria") String criteria) {
        return referenceService.retrievePollutants(criteria);
    }
    
    @GET
    @Path("sectors")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all sectors.")
    public List<Sector> retrieveSectors() {
        return referenceService.retrieveSectors();
    }
    
    @GET
    @Path("subsectors")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all subsectors.")
    public List<Subsector> retrieveSubsectors() {
        return referenceService.retrieveSubsectors();
    }

    @GET
    @Path("subsectors/{sectorCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all subsectors for passed in sector.")
    public List<Subsector> retrieveSubsectorsBySectorCode(@PathParam("sectorCode") String sectorCode) {
        return referenceService.retrieveSubsectorsBySectorCode(sectorCode);
    }

    @GET
    @Path("sics")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all sics.")
    public List<Sic> retrieveSics() {
        return referenceService.retrieveSics();
    }

    @GET
    @Path("sics/{subsectorCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all sics for passed in subsector.")
    public List<Sic> retrieveSicsBySubsectorCode(@PathParam("subsectorCode") String subsectorCode) {
        return referenceService.retrieveSicsBySubsectorCode(subsectorCode);
    }
}
