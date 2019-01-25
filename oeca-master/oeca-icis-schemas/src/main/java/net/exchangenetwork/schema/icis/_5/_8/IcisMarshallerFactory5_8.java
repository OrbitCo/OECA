package net.exchangenetwork.schema.icis._5._8;

import gov.epa.oeca.common.ApplicationException;
import net.exchangenetwork.schema.icis.IcisMarshallerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.Marshaller;


/**
 * @author dfladung
 */
@Component
public class IcisMarshallerFactory5_8 extends IcisMarshallerFactory {

    private static final Logger logger = LoggerFactory.getLogger(IcisMarshallerFactory5_8.class);

    public Marshaller getMarshaller() throws ApplicationException {
        try {
            return getMarshaller("5_8", Document.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
