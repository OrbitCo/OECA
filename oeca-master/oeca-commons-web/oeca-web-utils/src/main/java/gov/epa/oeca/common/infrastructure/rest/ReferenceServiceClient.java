package gov.epa.oeca.common.infrastructure.rest;


import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.ref.Tribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReferenceServiceClient extends BaseRestClient {

	private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceClient.class);	
	private static final String tribesResource = "/api/lookup/v1/tribes";
	
	public ReferenceServiceClient() {
    	super(null, logger);
    }

	public ReferenceServiceClient(String baseEndpointUri) {
    	super(baseEndpointUri, logger);
    }

    public Tribe retrieveTribeByLandNameAndStateCode(String tribalLandName, String stateCode) throws ApplicationException {
        try {
        	return template.getForObject(baseEndpointUri + tribesResource + "/{stateCode}/{tribalLandName}", Tribe.class, stateCode, tribalLandName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

}
