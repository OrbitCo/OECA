package gov.epa.oeca.common.domain.dto;

/**
 * Created by smckay on 1/27/2017.
 */
public class Lookup {
    private String code;
    private String description;

    public Lookup() {

    }

    public Lookup(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
