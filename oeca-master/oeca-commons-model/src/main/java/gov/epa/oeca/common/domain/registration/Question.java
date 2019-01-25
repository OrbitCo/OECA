package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

/**
 * @author dfladung
 */
public class Question extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Long id;
    String text;
    String type;
    String useType;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", useType='" + useType + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }
}
