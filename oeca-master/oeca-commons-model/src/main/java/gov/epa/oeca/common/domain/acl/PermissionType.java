package gov.epa.oeca.common.domain.acl;

/**
 * Created by kptucker on 7/19/2017.
 */
public enum PermissionType {

    None("None"),
    View("View"),
    Edit("Edit"),
    Sign("Sign"),
    Manage("Manage"),
    FormAccess("FormAccess");

    private final String label;

    PermissionType(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
