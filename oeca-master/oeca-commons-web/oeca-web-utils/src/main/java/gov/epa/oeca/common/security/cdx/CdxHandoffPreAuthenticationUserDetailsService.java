package gov.epa.oeca.common.security.cdx;

import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.infrastructure.cdx.register.StreamlinedRegistrationClient;
import gov.epa.oeca.common.infrastructure.naas.securitytoken.SecurityTokenClient;
import gov.epa.oeca.common.security.ApplicationUser;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationRoleDataElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author dfladung
 */
@Service
public class CdxHandoffPreAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    public static final String TITLE = "title";
    public static final String FIRST_NAME = "firstname";
    public static final String MIDDLE_INITIAL = "middleinitial";
    public static final String LAST_NAME = "lastname";
    public static final String SUFFIX = "suffix";
    public static final String ORG_NAME = "organization";
    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String ADDRESS3 = "address3";
    public static final String ADDRESS4 = "address4";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL_CODE = "postalcode";
    public static final String COUNTRY = "country";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phonenumber";
    public static final String PHONE_EXTENSION = "phoneextension";
    public static final String PROGRAM = "program";
    public static final String CLIENT_ID = "clientid";
    public static final String ORG_ID = "OrganizationId";
    public static final String USER_ORG_ID = "UserOrganizationId";
    public static final String USER_ROLE_ID = "UserRoleId";
    public static final String ROLE_ID = "RoleId";
    public static final String ID_TYPE_CODE = "idTypecode";
    public static final String ID_TYPE_TEXT = "idTypetext";

    private static final Logger logger = LoggerFactory.getLogger(CdxHandoffPreAuthenticationUserDetailsService.class);

    @Autowired
    SecurityTokenClient securityTokenClient;
    @Autowired
    StreamlinedRegistrationClient registrationClient;
    @Autowired
    RegistrationHelper registrationHelper;

    @Override
    @SuppressWarnings(value = "unchecked")
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken)
            throws UsernameNotFoundException {
        Map<String, String> params = null;
        try {
            URL endpoint = new URL(registrationHelper.getNaasEndpoint());
            params = securityTokenClient.validate(
                    endpoint,
                    registrationHelper.getNaasIssuer(),
                    registrationHelper.getNaasCredentials(),
                    preAuthenticatedAuthenticationToken.getPrincipal().toString(),
                    preAuthenticatedAuthenticationToken.getCredentials().toString());


        } catch (MalformedURLException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }

        ApplicationUser user = new ApplicationUser(params.get("uid"),
                preAuthenticatedAuthenticationToken.getCredentials().toString(),
                getRoles(params));
        user.setTitle(params.get(TITLE));
        user.setFirstName(params.get(FIRST_NAME));
        user.setMiddleInitial(params.get(MIDDLE_INITIAL));
        user.setLastName(params.get(LAST_NAME));
        user.setSuffix(params.get(SUFFIX));
        user.setOrganization(params.get(ORG_NAME));
        user.setAddress1(params.get(ADDRESS1));
        user.setAddress2(params.get(ADDRESS2));
        user.setAddress3(params.get(ADDRESS3));
        user.setAddress4(params.get(ADDRESS4));
        user.setCity(params.get(CITY));
        user.setState(params.get(STATE));
        user.setPostalCode(params.get(POSTAL_CODE));
        user.setCountry(params.get(COUNTRY));
        user.setEmail(params.get(EMAIL));
        user.setPhoneNumber(params.get(PHONE_NUMBER));
        user.setPhoneExtension(params.get(PHONE_EXTENSION));
        user.setDataflowName(params.get(PROGRAM));
        user.setClientId(params.get(CLIENT_ID));
        user.setOrganizationId(nullSafeLong(params.get(ORG_ID)));
        user.setUserOrganizationId(nullSafeLong(params.get(USER_ORG_ID)));
        user.setUserRoleId(nullSafeLong(params.get(USER_ROLE_ID)));
        user.setRoleId(nullSafeLong(params.get(ROLE_ID)));
        user.setIdTypeCode(nullSafeInt(params.get(ID_TYPE_CODE)));
        user.setIdTypeText(params.get(ID_TYPE_TEXT));
        Collection<String> roles = CollectionUtils.collect(user.getAuthorities(), Object::toString);

        try {
            URL registrationEndpoint = new URL(registrationHelper.getStreamlinedRegistrationServiceEndpoint());
            String token = registrationClient.authenticate(
                    registrationEndpoint,
                    registrationHelper.getStreamlinedRegisterOperatorUser(),
                    registrationHelper.getStreamlinedRegisterOperatorPassword());
            List<RegistrationRoleDataElement> userRoleDataElements = registrationClient.retrieveRoleDataElements(
                    registrationEndpoint, token, user.getUserRoleId());
            if (!CollectionUtils.isEmpty(userRoleDataElements)) {
                for (RegistrationRoleDataElement e : userRoleDataElements) {
                    switch (e.getElement().getCode()) {
                        case "JOB_TITLE":
                            user.setJobTitle(e.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Could not load user's role data elements", e);
        }

        logger.info(String.format("Loading %s with application roles %s",
                user.getUsername(), StringUtils.join(roles, ",")));
        return user;
    }

    protected Long nullSafeLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected Integer nullSafeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected Collection<SimpleGrantedAuthority> getRoles(Map<String, String> userProperties) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_OECA_USER"));
    }


}
