package gov.epa.oeca.common.infrastructure.naas.securitytoken;

import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.schema.securitytoken._3.AuthMethod;
import net.exchangenetwork.schema.securitytoken._3.DomainTypeCode;
import net.exchangenetwork.schema.securitytoken._3.TokenType;
import net.exchangenetwork.wsdl.securitytoken._3.SecurityTokenPortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component("authSecurityTokenClient")
public class SecurityTokenClient extends AbstractClient {
    public static final String TOKEN = "Token";
    public static final String IP = "CDX_DATA";
    private static final Logger logger = LoggerFactory.getLogger(SecurityTokenClient.class);

    public String extractTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(TOKEN);
    }

    public String extractIpFromRequest(HttpServletRequest request) {
        return request.getParameter(IP);
    }

    protected SecurityTokenPortType getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (SecurityTokenPortType) this.getClient(endpoint.toString(), SecurityTokenPortType.class, mtom, chunking);
    }

    public String createSecurityToken(URL endpoint, String trustee, String credential, DomainTypeCode domainType,
                                      TokenType tokenType, String issuer, AuthMethod authMethod, String subject,
                                      String subjectData, String ip) {
        return getClient(endpoint, false, false).createSecurityToken(trustee, credential, domainType, tokenType, issuer,
                authMethod, subject, subjectData, ip);
    }

    public Map<String, String> validate(URL endpoint, String trustee, String credential, String token, String ip) {
        String result = getClient(endpoint, false, false).validate(trustee, credential, DomainTypeCode.DEFAULT, token,
                ip, "");
        return parseParams(result);
    }

    Map<String, String> parseParams(String paramsUrl) {
        Map<String, String> results = new HashMap<>();
        String[] params = paramsUrl.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            String key = param[0];
            String value = (param.length < 2)
                    ? null
                    : param[1];
            results.put(key, value);
        }
        return results;
    }
}
