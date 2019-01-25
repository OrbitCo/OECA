package gov.epa.oeca.common.domain.components;

import java.io.File;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class BatchUpload implements Serializable {
	
	private static final long serialVersionUID = 1L;

    String name;    
    ZonedDateTime createdDate;    
    Long size;
    File data;    

    public BatchUpload() {
    }

    public BatchUpload(String name, File data, ZonedDateTime createdDate) {
        this.name = name;
        this.createdDate = createdDate;
        this.data = data;
        if (data != null && data.exists()){
            this.size = data.length();
        }
    }

    @Override
    public String toString() {
        return "BatchUpload{" +
                "name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", size=" + size +
                "} " + super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
        if (data != null && data.exists()){
            this.size = data.length();
        }
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
	
}