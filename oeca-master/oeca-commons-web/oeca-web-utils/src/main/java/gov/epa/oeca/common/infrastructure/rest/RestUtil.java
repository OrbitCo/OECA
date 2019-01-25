package gov.epa.oeca.common.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.security.jwt.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author dfladung
 */
@Component
public class RestUtil {

    private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);

    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    public <T> HttpEntity<T> getEntityWithJsonContentType(T input) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(input, headers);
    }

    @SuppressWarnings("unchecked")
    public <T> HttpEntity<T> getEntityWithJsonContentType(T input, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(JwtTokenUtil.JWT_TOKEN_HEADER, "Bearer " + token);
        return new HttpEntity<>(input, headers);
    }

    public ApplicationException translateException(HttpStatusCodeException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), ApplicationException.class);
        } catch (Exception ex) {
            logger.error("Could not translate REST client exception", e);
            throw new ApplicationException(ApplicationErrorCode.E_InternalError, e.getMessage());
        }
    }
}
