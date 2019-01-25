package gov.epa.oeca.common.domain.acl;

/**
 * Created by kptucker on 7/19/2017.
 */
public enum Module {

    GMG("GMG"), Biosolids("Biosolids");

    private final String label;

    Module(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
