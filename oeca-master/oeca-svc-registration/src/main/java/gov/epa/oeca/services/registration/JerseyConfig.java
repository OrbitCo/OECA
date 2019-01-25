package gov.epa.oeca.services.registration;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import gov.epa.oeca.common.interfaces.rest.CorsResponseFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
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
        register(provider);
        register(MultiPartFeature.class);
        register(CorsResponseFilter.class);
        packages("io.swagger.jaxrs.listing",
                "org.glassfish.jersey.examples.multipart",
                "gov.epa.oeca.services.registration.interfaces.rest"
        );


        logger.info("Initialized Jersey");
    }
}
