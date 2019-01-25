package gov.epa.oeca.common.domain.document;

import gov.epa.oeca.common.domain.BaseValueObject;

import java.io.File;

/**
 * @author dfladung
 */
public class Document extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String name;
    File content;

    public Document() {
    }

    public Document(String name, File content) {
        this.name = name;
        this.content = content;
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
