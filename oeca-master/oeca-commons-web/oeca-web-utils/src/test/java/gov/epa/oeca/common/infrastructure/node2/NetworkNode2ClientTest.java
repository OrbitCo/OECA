package gov.epa.oeca.common.infrastructure.node2;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.Transaction;
import gov.epa.oeca.common.domain.node.TransactionStatus;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kptucker on 12/20/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-web-config-test.xml"})
public class NetworkNode2ClientTest {

    private static final Logger logger = LoggerFactory.getLogger(NetworkNode2ClientTest.class);

    @Autowired
    NetworkNode2Client networkNode2Client;
    @Autowired
    RegistrationHelper helper;
    @Autowired
    ResourceLoader loader;

    File getFile(String file) throws Exception {
        return loader.getResource(file).getFile();
    }

    URL getEndpoint() throws Exception {
        return new URL(helper.getNetworkNode2ServiceEndpoint());
    }

    String getToken() throws Exception {
        return networkNode2Client.authenticate(
                getEndpoint(),
                helper.getNetworkNode2OperatorUser(),
                helper.getNetworkNode2OperatorPassword(),
                null,
                "default");
    }

    @Test
    public void testAuthenticate() throws Exception {
        try {
            String token = networkNode2Client.authenticate(getEndpoint(), helper.getNetworkNode2OperatorUser(),
                    helper.getNetworkNode2OperatorPassword(), null, "default");
            assertNotNull(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testAuthenticate_wrongPassword() throws Exception {
        try {
            networkNode2Client.authenticate(getEndpoint(), helper.getNetworkNode2OperatorUser(),
                    "bogus", null, "default");
            fail("Should not have authenticated using wrong password.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_RemoteServiceError, e.getErrorCode());
            assertTrue(e.getMessage().contains("The password is invalid."));
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testDownload() throws Exception {
        try {
            List<Document> documents = networkNode2Client.download(getEndpoint(), getToken(),
                    "_0046c49b-e092-465a-b0a1-b05fb129b290", "ICIS-NPDES");
            assertNotNull(documents);
            assertEquals(documents.size(), 2);
            assertTrue(documents.get(0).getContent().length() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetStatus() throws Exception {
        try {
            Transaction transaction = networkNode2Client.getStatus(getEndpoint(), getToken(),
                    "_0046c49b-e092-465a-b0a1-b05fb129b290");
            assertEquals("_0046c49b-e092-465a-b0a1-b05fb129b290", transaction.getTxId());
            assertEquals(TransactionStatus.FAILED, transaction.getStatus());
            assertEquals("E_InternalError: all files failed validation", transaction.getStatusDetail());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testSubmit() throws Exception {
        try {
            Document document = new Document();
            document.setName("ICIS.zip");
            document.setContent(getFile("ICIS.zip"));
            String transactionId = networkNode2Client.submit(getEndpoint(), getToken(), null,
                    "ICIS-NPDES", null, Collections.singletonList(document));
            assertNotNull(transactionId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

}
