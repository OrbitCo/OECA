package gov.epa.oeca.common.infrastructure.cdx.pdf;

import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import net.exchangenetwork.wsdl.register.pdf._1.HandoffType;
import net.exchangenetwork.wsdl.register.pdf._1.HandoffUserType;
import net.exchangenetwork.wsdl.register.pdf._1.PdfDocumentType;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-web-config-test.xml"})
public class RegisterPdfClientTest {

    private static final Logger logger = LoggerFactory.getLogger(RegisterPdfClientTest.class);

    @Autowired
    RegisterPdfClient pdfClient;
    @Autowired
    RegistrationHelper helper;

    @Test
    public void renderUrlAsPdf() throws Exception {
        try {
            URL endpoint = new URL(helper.getRegisterPdfServiceEndpoint());
            String admin = helper.getRegisterPdfOperatorUser();
            String adminPassword = helper.getRegisterPdfOperatorPassword();
            String token = pdfClient.authenticate(endpoint, admin, adminPassword);
            String target = "https://dev.epacdx.net/";
            HandoffUserType user = new HandoffUserType();
            user.setFirstName("David");
            user.setLastName("Fladung");
            user.setUserId("DFLADUNG");
            user.setRoleId(120420L);
            user.setUserRoleId(114132L);
            user.setOrganizationId(17233L);
            user.setUserOrganizationId(61799L);
            user.setIdTypeCode(1);
            PdfDocumentType result = pdfClient.renderUrlAsPdf(endpoint, token, target, HandoffType.RSO, user);
            File tmp = File.createTempFile("PdfClientTestResult", ".pdf");
            FileUtils.copyToFile(result.getContent().getInputStream(), tmp);
            assertTrue(tmp.length() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fail(e.getMessage());
        }
    }

}