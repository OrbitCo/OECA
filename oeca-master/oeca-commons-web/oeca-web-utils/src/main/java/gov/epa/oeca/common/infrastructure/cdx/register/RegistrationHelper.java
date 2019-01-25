package gov.epa.oeca.common.infrastructure.cdx.register;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author dfladung
 */
@Component
public class RegistrationHelper {

    @Resource(name = "registerAuthEndpointConfiguration")
    Map<String, String> registerAuthEndpointConfiguration;

    @Resource(name = "registerStreamlinedEndpointConfiguration")
    Map<String, String> registerStreamlinedEndpointConfiguration;

    @Resource(name = "registerSignEndpointConfiguration")
    Map<String, String> registerSignEndpointConfiguration;

    @Resource(name = "registerEndpointConfiguration")
    Map<String, String> registerEndpointConfiguration;

    @Resource(name = "naasEndpointConfiguration")
    Map<String, String> naasEndpointConfiguration;

    @Resource(name = "networkNode2EndpointConfiguration")
    Map<String, String> networkNode2EndpointConfiguration;

    @Resource(name = "registerPdfEndpointConfiguration")
    Map<String, String> registerPdfEndpointConfiguration;

    @Resource(name = "registerReviewerEndpointConfiguration")
    Map<String, String> registerReviewerEndpointConfiguration;

    @Resource(name = "registerInboxEndpointConfiguration")
    Map<String, String> registerInboxEndpointConfiguration;

    @Resource(name = "baseConfig")
    Map<String, String> baseConfig;

    public String getAuthOperatorUser() {
        return registerAuthEndpointConfiguration.get("operatorId");
    }

    public String getAuthOperatorPassword() {
        return registerAuthEndpointConfiguration.get("operatorPassword");
    }

    public String getAuthServiceEndpoint() {
        return registerAuthEndpointConfiguration.get("serviceUrl");
    }

    public String getStreamlinedRegisterOperatorUser() {
        return registerStreamlinedEndpointConfiguration.get("operatorId");
    }

    public String getStreamlinedRegisterOperatorPassword() {
        return registerStreamlinedEndpointConfiguration.get("operatorPassword");
    }

    public String getStreamlinedRegistrationServiceEndpoint() {
        return registerStreamlinedEndpointConfiguration.get("serviceUrl");
    }

    public String getSignatureOperatorUser() {
        return registerSignEndpointConfiguration.get("operatorId");
    }

    public String getSignatureOperatorPassword() {
        return registerSignEndpointConfiguration.get("operatorPassword");
    }

    public String getSignatureServiceEndpoint() {
        return registerSignEndpointConfiguration.get("serviceUrl");
    }

    public String getRegisterOperatorUser() { return registerEndpointConfiguration.get("operatorId"); }

    public String getRegisterOperatorPassword() { return registerEndpointConfiguration.get("operatorPassword"); }

    public String getRegisterServiceEndpoint() { return registerEndpointConfiguration.get("serviceUrl"); }

    public String getRegisterPdfOperatorUser() { return registerPdfEndpointConfiguration.get("operatorId"); }

    public String getRegisterPdfOperatorPassword() { return registerPdfEndpointConfiguration.get("operatorPassword"); }

    public String getRegisterPdfServiceEndpoint() { return registerPdfEndpointConfiguration.get("serviceUrl"); }

    public String getRegisterReviewerOperatorUser() { return registerReviewerEndpointConfiguration.get("operatorId"); }

    public String getRegisterReviewerOperatorPassword() { return registerReviewerEndpointConfiguration.get("operatorPassword"); }

    public String getRegisterReviewerServiceEndpoint() { return registerReviewerEndpointConfiguration.get("serviceUrl"); }

    public String getNaasIssuer() { return naasEndpointConfiguration.get("issuer"); }

    public String getNaasCredentials() { return naasEndpointConfiguration.get("credentials"); }

    public String getNaasDomain() { return naasEndpointConfiguration.get("domain"); }

    public String getNaasIp() { return naasEndpointConfiguration.get("ip"); }

    public String getNaasEndpoint() { return naasEndpointConfiguration.get("serviceUrl"); }

    public String getNetworkNode2OperatorUser() {
        return networkNode2EndpointConfiguration.get("operatorId");
    }

    public String getNetworkNode2OperatorPassword() {
        return networkNode2EndpointConfiguration.get("operatorPassword");
    }

    public String getNetworkNode2ServiceEndpoint() {
        return networkNode2EndpointConfiguration.get("serviceUrl");
    }

    public String getInboxOperatorUser() {
        return registerInboxEndpointConfiguration.get("operatorId");
    }

    public String getInboxOperatorPassword() {
        return registerInboxEndpointConfiguration.get("operatorPassword");
    }

    public String getInboxServiceEndpoint() {
        return registerInboxEndpointConfiguration.get("serviceUrl");
    }

    public String getBaseCdxUrl() {
        return baseConfig.get("baseCdxUrl");
    }
}
