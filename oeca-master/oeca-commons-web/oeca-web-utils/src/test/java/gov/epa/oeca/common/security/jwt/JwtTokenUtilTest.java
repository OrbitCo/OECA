package gov.epa.oeca.common.security.jwt;

import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author dfladung
 */
public class JwtTokenUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtilTest.class);

    JwtTokenUtil jwtTokenUtil;

    @Before
    public void setup() {
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    public void createTokenForUser() throws Exception {
        try {
            String token = jwtTokenUtil.createTokenForUser(
                    "DFLADUNG", Collections.singletonList("ROLE_OECA_USER"));
            Assert.isTrue(!StringUtils.isEmpty(token));
            logger.info("generated token " + token);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}