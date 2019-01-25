package gov.epa.oeca.common.infrastructure.batch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.epa.oeca.common.domain.components.BatchUploadSummary;
import gov.epa.oeca.common.domain.components.BatchUploadSummary.RowError;

public class BaseBatchUploadService {

	private static final Logger logger = LoggerFactory.getLogger(BaseBatchUploadService.class);

    protected boolean validateMaxLength(String val, int max, BatchUploadSummary summary, long row, String column) {
    	boolean valid = true;
    	if ((!StringUtils.isEmpty(val)) && (val.length() > max)) {
    		summary.getErrors().add(summary.new RowError(row, column, "Value must be a maximum of " + max + " characters."));
    		valid = false;
    	}
    	return valid;
    }

    protected boolean validateRequired(String val, BatchUploadSummary summary, long row, String column) {
    	boolean valid = true;
    	if (StringUtils.isEmpty(val)) {
    		summary.getErrors().add(summary.new RowError(row, column, "Value is required."));
    		valid = false;
    	}
    	return valid;
    }

    protected boolean validateNumeric(String val, BatchUploadSummary summary, long row, String column) {
    	boolean valid = true;
    	if ((!StringUtils.isEmpty(val)) && (!val.matches("[0-9]+"))) {
    		summary.getErrors().add(summary.new RowError(row, column, "Value must be numeric."));
    		valid = false;
    	}
    	return valid;
    }

    protected boolean validateEmail(String val, BatchUploadSummary summary, long row, String column) {
    	boolean valid = true;
    	if ((!StringUtils.isEmpty(val)) && (!val.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))) {
    		summary.getErrors().add(summary.new RowError(row, column, "Value must be valid email address."));
    		valid = false;
    	}
    	return valid;
    }

    protected boolean validatePhoneUS(String val, BatchUploadSummary summary, long row, String column) {
    	boolean valid = true;
    	if ((!StringUtils.isEmpty(val)) && (!val.matches("^(1)?[2-9]\\d{2}[2-9]\\d{2}\\d{4}$"))) {
    		summary.getErrors().add(summary.new RowError(row, column, "Value must be valid US phone number."));
    		valid = false;
    	}
    	return valid;
    }

    protected boolean validateYN(String val, BatchUploadSummary summary, long row, String column) {
    	boolean valid = true;
    	if ((!StringUtils.isEmpty(val)) && (!(val.equals("Y") || val.equals("N")))) {
    		summary.getErrors().add(summary.new RowError(row, column, "Value must be Y or N."));
    		valid = false;
    	}
    	return valid;
    }
    
    protected String generateProcessingReport(BatchUploadSummary summary) {
    	String processingReport;
    	
    	processingReport = "Processing Summary:\n";
    	processingReport += "-------------------\n\n";
    	processingReport += "Draft forms created: " + summary.getSuccess() + "\n";
    	processingReport += "Forms failing validation: " + summary.getFailure() + "\n\n";
        
        if (!CollectionUtils.isEmpty(summary.getErrors())) {
        	processingReport += "Error Details:\n";
        	processingReport += "--------------\n\n";
        	
        	for (RowError r : summary.getErrors()) {
        		processingReport += "Row " + r.getRow() + ", column " + r.getColumn() + ": " + r.getReason() + "\n";
        	}
        }
    	
    	return processingReport;
    }

}
