package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dfladung
 */
public class NewUserProfile extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    User user;
    Organization organization;
    Role role;
    List<Answer> secretAnswers;
    List<Answer> electronicSignatureAnswers;

    public NewUserProfile() {
        secretAnswers = new ArrayList<>();
        electronicSignatureAnswers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "NewUserProfile{" +
                "user=" + user +
                ", organization=" + organization +
                ", role=" + role +
                '}';
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Answer> getSecretAnswers() {
        return secretAnswers;
    }

    public void setSecretAnswers(List<Answer> secretAnswers) {
        this.secretAnswers = secretAnswers;
    }

    public List<Answer> getElectronicSignatureAnswers() {
        return electronicSignatureAnswers;
    }

    public void setElectronicSignatureAnswers(List<Answer> electronicSignatureAnswers) {
        this.electronicSignatureAnswers = electronicSignatureAnswers;
    }
}
