package gov.epa.oeca.services.ref.application;

import gov.epa.oeca.common.domain.ref.*;
import gov.epa.oeca.common.ApplicationException;

import java.util.List;

public interface ReferenceService {
    List<State> retrieveStates() throws ApplicationException;

    List<State> retrieveStates(Integer region) throws ApplicationException;

    State retrieveState(String stateCode) throws ApplicationException;

    List<Tribe> retrieveTribes() throws ApplicationException;

    List<Tribe> retrieveTribes(String stateCode) throws ApplicationException;

    List<Tribe> retrieveTribesByLandName(String tribalLandName) throws ApplicationException;

    Tribe retrieveTribeByLandNameAndStateCode(String tribalLandName, String stateCode) throws ApplicationException;

    List<BiaTribe> retrieveBiaTribes() throws ApplicationException;

    List<BiaTribe> retrieveBiaTribes(String stateCode) throws ApplicationException;

    List<County> retrieveCounties(String stateCode) throws ApplicationException;

    List<County> retrieveAllCounties() throws ApplicationException;

    List<Chemical> retrieveChemicals() throws ApplicationException;

    List<Chemical> retrieveChemicals(String name) throws ApplicationException;

    List<Pollutant> retrievePollutants(String name) throws ApplicationException;
    
    List<Sector> retrieveSectors() throws ApplicationException;
    
    List<Subsector> retrieveSubsectors() throws ApplicationException;
    
    List<Subsector> retrieveSubsectorsBySectorCode(String sectorCode) throws ApplicationException;
    
    List<Sic> retrieveSics() throws ApplicationException;

    List<Sic> retrieveSicsBySubsectorCode(String subsectorCode) throws ApplicationException;
}
