package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

/**
 * @author dfladung
 */
public class Esa extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    User user;
    Organization organization;
    String approver;
    EsaStatus status;
    EsaType type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public EsaStatus getStatus() {
        return status;
    }

    public void setStatus(EsaStatus status) {
        this.status = status;
    }

    public EsaType getType() {
        return type;
    }

    public void setType(EsaType type) {
        this.type = type;
    }
}
