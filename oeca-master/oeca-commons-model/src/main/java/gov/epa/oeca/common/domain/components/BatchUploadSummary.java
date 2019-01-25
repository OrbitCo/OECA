package gov.epa.oeca.common.domain.components;

import java.io.Serializable;
import java.util.ArrayList;

public class BatchUploadSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;

    long success;    
    long failure;    
    ArrayList<RowError> errors;
    String processingReport;
    long total;
    Boolean scheduled;

    public BatchUploadSummary() {
    }

    @Override
    public String toString() {
        return "BatchUploadSummary{" +
                "success=" + success + 
                ", failure=" + failure + 
                "} " + super.toString();
    }

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	public long getFailure() {
		return failure;
	}

	public void setFailure(long failure) {
		this.failure = failure;
	}

	public ArrayList<RowError> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<RowError> errors) {
		this.errors = errors;
	}	

	public String getProcessingReport() {
		return processingReport;
	}

	public void setProcessingReport(String processingReport) {
		this.processingReport = processingReport;
	}
		
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}


	public class RowError implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		long row;		
		String column;		
		String reason;
		
		public RowError() {			
		}
		
		public RowError(long row, String column, String reason) {
			this.row = row;
			this.column = column;
			this.reason = reason;
		}

		public long getRow() {
			return row;
		}

		public void setRow(long row) {
			this.row = row;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

	}
}