package gov.epa.oeca.common.infrastructure.rest;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;

import org.slf4j.Logger;

public abstract class BaseRestClient {

    protected RestTemplate template;
    protected String baseEndpointUri;
    
    public BaseRestClient(Logger logger) {
        try {
        	template = new RestTemplate();
            template.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                    return !clientHttpResponse.getStatusCode().series().equals(HttpStatus.Series.SUCCESSFUL);
                }

                @Override
                public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                    logger.error(IOUtils.toString(clientHttpResponse.getBody(), "UTF-8"));
                	throw new ApplicationException(ApplicationErrorCode.E_RemoteServiceError, IOUtils.toString(clientHttpResponse.getBody(), "UTF-8")); 
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        	throw new ApplicationException(ApplicationErrorCode.E_InternalError, e.getMessage());
        }
    }

    public BaseRestClient(String baseEndpointUri, Logger logger) {
        this(logger);
    	this.baseEndpointUri = baseEndpointUri;
    }

	public String getBaseEndpointUri() {
		return baseEndpointUri;
	}

	public void setBaseEndpointUri(String baseEndpointUri) {
		this.baseEndpointUri = baseEndpointUri;
	}        

}
