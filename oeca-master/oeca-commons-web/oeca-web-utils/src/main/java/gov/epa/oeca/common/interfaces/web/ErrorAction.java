package gov.epa.oeca.common.interfaces.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * @author dfladung
 */
@UrlBinding(value = "/action/error")
public class ErrorAction extends BaseAction {

    private String errorMessage;
    private String errorId;

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution("/error.jsp");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
}
