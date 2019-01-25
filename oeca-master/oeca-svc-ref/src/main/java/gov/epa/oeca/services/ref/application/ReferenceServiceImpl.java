package gov.epa.oeca.services.ref.application;

import gov.epa.oeca.common.domain.ref.*;
import gov.epa.oeca.services.ref.infrastructure.persistence.ReferenceRepository;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author smckay
 */
@Service("referenceService")
@Transactional(readOnly = true)
public class ReferenceServiceImpl implements ReferenceService {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    @Autowired
    ReferenceRepository referenceRepository;

    @Override
    @Cacheable("states")
    public List<State> retrieveStates() throws ApplicationException {
        try {
            return referenceRepository.retrieveStates();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("states")
    public State retrieveState(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required.");
            return referenceRepository.retrieveState(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("states")
    public List<State> retrieveStates(Integer region) throws ApplicationException {
        try {
            Validate.notNull(region, "Region is required.");
            Validate.isTrue(region >= 1 && region <= 11, "Only 1-11 are valid region codes.");
            return referenceRepository.retrieveStates(region);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("tribes")
    public List<Tribe> retrieveTribes() throws ApplicationException {
        try {
            return referenceRepository.retrieveTribes();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("tribes")
    public List<Tribe> retrieveTribes(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required to retrieve tribes.");
            return referenceRepository.retrieveTribes(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Tribe> retrieveTribesByLandName(String tribalLandName) throws ApplicationException {
        try {
            Validate.notEmpty(tribalLandName, "Tribal land name is required");
            return referenceRepository.retrieveTribesByLandName(tribalLandName);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Tribe retrieveTribeByLandNameAndStateCode(String tribalLandName, String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(tribalLandName, "Tribal land name is required");
            Validate.notEmpty(stateCode, "State code is required");
            return referenceRepository.retrieveTribeByLandNameAndStateCode(tribalLandName, stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("biaTribes")
    public List<BiaTribe> retrieveBiaTribes() throws ApplicationException {
        try {
            return referenceRepository.retrieveBiaTribes();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("biaTribes")
    public List<BiaTribe> retrieveBiaTribes(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required to retrieve tribes.");
            return referenceRepository.retrieveBiaTribes(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("counties")
    public List<County> retrieveAllCounties() throws ApplicationException {
        try {
            return referenceRepository.retrieveCounties();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("counties")
    public List<County> retrieveCounties(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required to retrieve counties.");
            return referenceRepository.retrieveCounties(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("chemicals")
    public List<Chemical> retrieveChemicals() throws ApplicationException {
        try {
            return referenceRepository.retrieveActiveChemicals();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("chemicals")
    public List<Chemical> retrieveChemicals(String name) throws ApplicationException {
        try {
            Validate.notEmpty(name, "Chemical name is required.");
            Validate.isTrue(
                    name.length() >= 3,
                    "At least 3 characters are required to search by chemical name.");
            return referenceRepository.retrieveActiveChemicals(name);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Pollutant> retrievePollutants(String name) throws ApplicationException {
        try {
            Validate.notEmpty(name, "Pollutant name is required.");
            Validate.isTrue(
                    name.length() >= 3,
                    "At least 3 characters are required to search by pollutant name.");
            return referenceRepository.retrievePollutants(name);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

	@Override
	public List<Sector> retrieveSectors() throws ApplicationException {
        try {
            return referenceRepository.retrieveSectors();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
	}

	@Override
	public List<Subsector> retrieveSubsectors() throws ApplicationException {
        try {
            return referenceRepository.retrieveSubsectors();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
	}

	@Override
	public List<Subsector> retrieveSubsectorsBySectorCode(String sectorCode) throws ApplicationException {
        try {
            Validate.notEmpty(sectorCode, "Sector code is required to retrieve subsectors.");
            return referenceRepository.retrieveSubsectorsBySectorCode(sectorCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
	}

	@Override
	public List<Sic> retrieveSics() throws ApplicationException {
        try {
            return referenceRepository.retrieveSics();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
	}

	@Override
	public List<Sic> retrieveSicsBySubsectorCode(String subsectorCode) throws ApplicationException {
        try {
            Validate.notEmpty(subsectorCode, "Subsector code is required to retrieve sic codes.");
            return referenceRepository.retrieveSicsBySubsectorCode(subsectorCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
	}

}
