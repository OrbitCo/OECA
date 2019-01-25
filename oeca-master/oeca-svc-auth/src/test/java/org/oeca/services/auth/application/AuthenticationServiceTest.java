package org.oeca.services.auth.application;

import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.services.auth.application.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-svc-auth-test.xml"})
public class AuthenticationServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceTest.class);

    private static final String USER = "JUNIT_675793A2-FF2C-4842-9AEA-3F441C8271CB";
    private static final String PASSWORD = "Welcome1c";

    @Autowired
    AuthenticationService authenticationService;

    @Test
    public void testAuthenticate() {
        try {
            User user = new User();
            user.setUserId(USER);
            user.setPassword(PASSWORD);
            authenticationService.authenticate(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testGenerateHandoffToken() {
        try {
            User user = new User();
            user.setUserId(USER);
            user.setPassword(PASSWORD);
            String token = authenticationService.generateHandoffToken(user);
            logger.info(token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}
