package gov.epa.oeca.common.domain;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;
import java.io.File;

@MappedSuperclass
public abstract class BaseCromerrEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cromerr_activity_id", length = 37)
    String cromerrActivityId;

    @Column(name = "cromerr_document_name", length = 255)
    String cromerrDocumentName;

    @Column(name = "cromerr_document_id", length = 36)
    String cromerrDocumentId;

    @Column(name = "cromerr_document_size")
    Long cromerrDocumentSize;

    @Transient
    File data;
    
    public String getCromerrActivityId() {
        return cromerrActivityId;
    }

    public void setCromerrActivityId(String cromerrActivityId) {
        this.cromerrActivityId = cromerrActivityId;
    }

    public String getCromerrDocumentName() {
		return cromerrDocumentName;
	}

	public void setCromerrDocumentName(String cromerrDocumentName) {
		this.cromerrDocumentName = cromerrDocumentName;
	}

	public String getCromerrDocumentId() {
		return cromerrDocumentId;
	}

	public void setCromerrDocumentId(String cromerrDocumentId) {
		this.cromerrDocumentId = cromerrDocumentId;
	}

	public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
        if (data != null && data.exists()){
            this.cromerrDocumentSize = data.length();
        }
    }

	public Long getCromerrDocumentSize() {
		return cromerrDocumentSize;
	}

	public void setCromerrDocumentSize(Long cromerrDocumentSize) {
		this.cromerrDocumentSize = cromerrDocumentSize;
	}

}
