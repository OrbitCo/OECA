package gov.epa.oeca.services.ref;

import io.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class SwaggerConfig extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1");
        beanConfig.setTitle("Reference Services");
        beanConfig.setSchemes(new String[]{"http", "https"});
        beanConfig.setBasePath("/oeca-svc-ref/api");
        beanConfig.setResourcePackage("gov.epa.oeca.services.ref.interfaces.rest");
        beanConfig.setScan(true);
    }
}
