package gov.epa.oeca.common.domain.dto;

import java.io.Serializable;

public class ErrorEntry implements Serializable {

    private static final long serialVersionUID = 1L;
       
    private String identifier;
    private String error;
    
    public ErrorEntry() {
    }
    
    public ErrorEntry(String identifier, String error) {
    	this.identifier = identifier;
    	this.error = error;
    }
    
	@Override
    public String toString() {
        return "ErrorEntry{" +
                "identifier='" + identifier + '\'' +
                ", error='" + error + '\'' +
                "}";
    }

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
