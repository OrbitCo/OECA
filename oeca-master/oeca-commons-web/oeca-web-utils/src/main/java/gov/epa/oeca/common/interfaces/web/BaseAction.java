package gov.epa.oeca.common.interfaces.web;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author dfladung
 */
public class BaseAction implements ActionBean {

    private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);

    protected DefaultContext context;

    @SpringBean("baseConfig")
    protected Map<String, String> baseConfig;

    public DefaultContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (DefaultContext) context;
    }

    public String getAuthBaseUrl() {
        return baseConfig.get("authBaseUrl");
    }

    public String getRegistrationBaseUrl() {
        return baseConfig.get("registrationBaseUrl");
    }

    public String getRefBaseUrl() {
        return baseConfig.get("refBaseUrl");
    }

    public String getCdxForgotUserUrl() {
        return baseConfig.get("cdxForgotUser");
    }

    public String getCdxForgotPassUrl() {
        return baseConfig.get("cdxForgotPass");
    }

    public String getCdxActivateAccountUrl() { return baseConfig.get("cdxActivateAccount"); }

    public String getBaseCdxUrl() {return baseConfig.get("baseCdxUrl");}

}
