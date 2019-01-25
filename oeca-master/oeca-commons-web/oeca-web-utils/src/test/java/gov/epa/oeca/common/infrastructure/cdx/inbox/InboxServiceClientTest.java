package gov.epa.oeca.common.infrastructure.cdx.inbox;

import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.cdx.register.Assembler;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import net.exchangenetwork.wsdl.register.inbox._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.Collections;

import static org.junit.Assert.fail;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-web-config-test.xml"})
public class InboxServiceClientTest {

    private static final Logger logger = LoggerFactory.getLogger(InboxServiceClientTest.class);

    @Autowired
    InboxServiceClient inboxServiceClient;
    @Autowired
    RegistrationHelper helper;
    @Autowired
    ResourceLoader loader;
    @Autowired
    Assembler assembler;

    @Test
    public void createInboxMessageWithAttachments() throws  Exception {
        try {
            URL endpoint = new URL(helper.getInboxServiceEndpoint());
            String admin = helper.getInboxOperatorUser();
            String adminPassword = helper.getInboxOperatorPassword();
            String token = inboxServiceClient.authenticate(endpoint, admin, adminPassword);
            User user = new User();
            user.setFirstName("Linera");
            user.setLastName("Abieva");
            user.setUserId("LABIEVA34");
            user.setTitle("Ms");
            String from = "Unit Test";
            String subject = "Unit test subject";
            String message = "Unit test message";
            String dataflow = "NDMR-WY-001";
            InboxAttachments attachments = assembler.assembleInboxAttachments(
                    Collections.singletonList(loader.getResource("logback.xml").getFile()));
            inboxServiceClient.createInboxMessageWithAttachments(endpoint, token,
                    user, from, subject, message, dataflow, attachments);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }
}
