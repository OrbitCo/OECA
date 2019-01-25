package org.oeca.services.ref.application;

import gov.epa.oeca.common.domain.ref.County;
import gov.epa.oeca.common.domain.ref.State;
import gov.epa.oeca.services.ref.application.ReferenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.fail;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-svc-ref-test.xml"})
public class ReferenceServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceTest.class);

    @Autowired
    ReferenceService referenceService;

    @Test
    public void testRetrieveState() {
        try {
            State vaState = referenceService.retrieveState("VA");
            logger.info("VA State Name: " + vaState.getStateName());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveCounties() {
        try {
            List<County> counties = referenceService.retrieveCounties("VA");
            logger.info("VA County Count: " + counties.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}
