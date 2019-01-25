package gov.epa.oeca.common.domain.acl;

/**
 * Created by kptucker on 7/19/2017.
 */
public enum RequestAction {

    Grant("Grant"),
    Revoke("Revoke");

    private final String label;

    RequestAction(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
