package gov.epa.oeca.common.interfaces.web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dfladung
 */
public class ApplicationVersionServlet implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationVersionServlet.class);


    @Override
    public void contextInitialized(ServletContextEvent event) {
        InputStream is = null;
        try {
            is = ApplicationVersionServlet.class.getClassLoader().getResourceAsStream("runtime.properties");
            Properties props = new Properties();
            props.load(is);
            logger.info("Application version: " + props.get("application.version"));
        } catch (Exception e) {
            logger.warn("Could not determine application version.");
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
