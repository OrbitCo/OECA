package gov.epa.oeca.common.domain.acl;

public enum NotificationStatus {

    Y("Notification Sent"),
    N("Notification Pending"),
    R("Notification Retry"),
    X("Notification Error");

    private final String label;

    NotificationStatus(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
