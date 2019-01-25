package gov.epa.oeca.services.acl.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author dfladung
 */
public class TokenCredentials implements Serializable {

    private String applicationId;
    private String applicationKey;

    @JsonCreator
    public TokenCredentials(@JsonProperty("applicationId") String applicationId, @JsonProperty("applicationKey") String applicationKey) {
        this.applicationId = applicationId;
        this.applicationKey = applicationKey;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }
}