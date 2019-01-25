package gov.epa.oeca.common.security.cdx;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.infrastructure.naas.securitytoken.SecurityTokenClient;
import gov.epa.oeca.common.security.ApplicationUser;
import net.exchangenetwork.schema.securitytoken._3.AuthMethod;
import net.exchangenetwork.schema.securitytoken._3.DomainTypeCode;
import net.exchangenetwork.schema.securitytoken._3.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@Service
public class OecaUserHandoffServiceImpl implements OecaUserHandoffService {

    private static final Logger logger = LoggerFactory.getLogger(OecaUserHandoffServiceImpl.class);

    @Autowired
    RegistrationHelper registrationHelper;
    @Resource(name = "authSecurityTokenClient")
    SecurityTokenClient securityTokenClient;

    @Override
    public String generateHandoffToken(String userName, ApplicationUser user) {
        String naasToken = createToken(userName, user);
        return Base64.getEncoder().encodeToString(
                String.format("token=%s&remoteIpAddress=%s", naasToken, registrationHelper.getNaasIp())
                        .getBytes(Charset.forName("UTF-16LE")));
    }

    public String createToken(String userName, ApplicationUser user) {
        try {
            String userUpper = org.apache.commons.lang3.StringUtils.upperCase(userName);
            Map<String, String> data = new HashMap<>();
            data.put("userid", userName);
            if (user != null) {
                data.put("title", user.getTitle());
                data.put("firstname", user.getFirstName());
                data.put("middleinitial", user.getMiddleInitial());
                data.put("lastname", user.getLastName());
                data.put("suffix", user.getSuffix());
                data.put("organization", user.getOrganization());
                data.put("address1", user.getAddress1());
                data.put("address2", user.getAddress2());
                data.put("address3", user.getAddress3());
                data.put("address4", user.getAddress4());
                data.put("city", user.getCity());
                data.put("state", user.getState());
                data.put("postalcode", user.getPostalCode());
                data.put("country", user.getCountry());
                data.put("email", user.getEmail());
                data.put("phonenumber", user.getPhoneNumber());
                data.put("phoneextension", user.getPhoneExtension());
                data.put("program", user.getDataflowName());
                data.put("clientid", user.getClientId());

                if (user.getOrganizationId() != null) {
                    data.put("OrganizationId", user.getOrganizationId().toString());
                }
                if (user.getUserOrganizationId() != null) {
                    data.put("UserOrganizationId", user.getUserOrganizationId().toString());
                }
                if (user.getUserRoleId() != null) {
                    data.put("UserRoleId", user.getUserRoleId().toString());
                }
                if (user.getRoleId() != null) {
                    data.put("RoleId", user.getRoleId().toString());
                }

                if (user.getIdTypeCode() != null) {
                    data.put("idTypecode", user.getIdTypeCode().toString());
                }
                data.put("idTypetext", user.getIdTypeText());
            }
            StringBuffer dataString = new StringBuffer();
            String pattern = "{0}={1}&";
            for (Map.Entry<String, String> entry : data.entrySet()) {
                dataString.append(MessageFormat.format(pattern, entry.getKey(), entry.getValue()));
            }
            dataString.deleteCharAt(dataString.length() - 1);
            final String subjectData = dataString.toString();

            return securityTokenClient.createSecurityToken(
                    new URL(registrationHelper.getNaasEndpoint()),
                    registrationHelper.getNaasIssuer(),
                    registrationHelper.getNaasCredentials(),
                    DomainTypeCode.DEFAULT,
                    TokenType.CSM,
                    registrationHelper.getNaasIssuer(),
                    AuthMethod.PASSWORD,
                    userUpper,
                    subjectData,
                    registrationHelper.getNaasIp());
        } catch (Exception e) {
            logger.error("Error creating security token.", e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
