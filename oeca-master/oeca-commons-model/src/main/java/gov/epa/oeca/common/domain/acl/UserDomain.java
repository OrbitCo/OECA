package gov.epa.oeca.common.domain.acl;

/**
 * Created by kptucker on 7/19/2017.
 */
public enum UserDomain {

    CDX("CDX");

    private final String label;

    UserDomain(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
