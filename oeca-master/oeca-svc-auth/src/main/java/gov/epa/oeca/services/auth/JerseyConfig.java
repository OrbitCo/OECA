package gov.epa.oeca.services.auth;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import gov.epa.oeca.common.interfaces.rest.CorsResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dfladung
 */
public class JerseyConfig extends ResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig() {
        // initialize provder and register Joda provider
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        JodaMapper jodaMapper = new JodaMapper();
        jodaMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        provider.setMapper(jodaMapper);
        register(provider);
        register(CorsResponseFilter.class);
        packages("io.swagger.jaxrs.listing", "gov.epa.oeca.services.auth.interfaces.rest");
        logger.info("Initialized Jersey");
    }

}
