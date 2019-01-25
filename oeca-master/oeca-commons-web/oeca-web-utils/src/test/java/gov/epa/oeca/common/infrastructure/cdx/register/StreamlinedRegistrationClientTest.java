package gov.epa.oeca.common.infrastructure.cdx.register;

import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationNewUserProfile;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationRoleType;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationUserSearchCriteria;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationUserSearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-web-config-test.xml"})
public class StreamlinedRegistrationClientTest {

    private static final Logger logger = LoggerFactory.getLogger(StreamlinedRegistrationClientTest.class);

    @Autowired
    StreamlinedRegistrationClient client;
    @Autowired
    RegistrationHelper helper;

    @Test
    public void retrieveUsersByCriteria() throws Exception {
        try {
            URL endpoint = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String admin = helper.getStreamlinedRegisterOperatorUser();
            String adminPassword = helper.getStreamlinedRegisterOperatorPassword();
            String token = client.authenticate(endpoint, admin, adminPassword);

            RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
            criteria.setSubject("3");
            RegistrationRoleType role = new RegistrationRoleType();
            role.setCode(120430L);
            criteria.setRoleType(role);
            RegistrationUserSearchResult results = client.retrieveUsersByCriteria(endpoint, token, criteria, 0L, 100L, null);
            assertTrue(results.getTotalCount() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

}