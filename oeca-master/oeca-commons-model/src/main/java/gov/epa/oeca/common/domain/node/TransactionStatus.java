package gov.epa.oeca.common.domain.node;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
public enum TransactionStatus {

    RECEIVED("Received"),
    PROCESSING("Processing"),
    PENDING("Pending"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    APPROVED("Approved"),
    PROCESSED("Processed"),
    COMPLETED("Completed"),
    UNKNOWN("Unknown");

    private final String value;

    TransactionStatus(String label) {
        this.value = label;
    }

    public String getValue() {
        return value;
    }

    public static TransactionStatus fromValue(String v) {
        for (TransactionStatus c: TransactionStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
