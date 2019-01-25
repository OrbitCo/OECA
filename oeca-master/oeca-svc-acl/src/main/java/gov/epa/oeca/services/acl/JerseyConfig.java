package gov.epa.oeca.services.acl;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyConfig extends ResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig() {
        // initialize provder and register Joda provider
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        JodaMapper jodaMapper = new JodaMapper();
        jodaMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jodaMapper.registerModule(new JavaTimeModule());
        provider.setMapper(jodaMapper);        
        register(provider);
        
        packages("io.swagger.jaxrs.listing", "gov.epa.oeca.services.acl.interfaces.rest");
        logger.info("Initialized Jersey");
    }

}
