package gov.epa.oeca.common.domain.acl;

/**
 * Created by kptucker on 7/19/2017.
 */
public enum RequestStatus {

    Pending("Pending"),
    Approved("Approved"),
    Rejected("Rejected"),
    Cancelled("Cancelled");

    private final String label;

    RequestStatus(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
