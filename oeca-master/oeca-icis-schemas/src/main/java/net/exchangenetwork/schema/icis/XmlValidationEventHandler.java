package net.exchangenetwork.schema.icis;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

/**
 * @author dfladung
 */
public class XmlValidationEventHandler implements ValidationEventHandler {

    @Override
    public boolean handleEvent(ValidationEvent event) {
        if (event.getSeverity() == ValidationEvent.ERROR || event.getSeverity() == ValidationEvent.FATAL_ERROR) {
            ValidationEventLocator locator = event.getLocator();
            throw new ApplicationException(ApplicationErrorCode.E_Validation, String.format(
                    "XML validation exception: %s at row %s, column %s", event.getMessage(), locator.getLineNumber(),
                    locator.getColumnNumber()));
        }
        return true;
    }

}
