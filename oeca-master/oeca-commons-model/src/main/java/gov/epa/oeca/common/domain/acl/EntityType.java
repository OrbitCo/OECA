package gov.epa.oeca.common.domain.acl;

/**
 * Created by kptucker on 7/19/2017.
 */
public enum EntityType {

    Structure("Structure"),
    Operator("Operator"),
    Facility("Facility"),
    ProgramReport("ProgramReport");

    private final String label;

    EntityType(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
