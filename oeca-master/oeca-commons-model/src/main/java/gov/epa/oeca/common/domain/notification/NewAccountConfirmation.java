package gov.epa.oeca.common.domain.notification;

import gov.epa.oeca.common.domain.BaseValueObject;

/**
 * @author dfladung
 */
public class NewAccountConfirmation extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String user;
    String email;
    String confirmationCode;

    public NewAccountConfirmation() {
    }

    public NewAccountConfirmation(String user, String email, String confirmationCode) {
        this.user = user;
        this.email = email;
        this.confirmationCode = confirmationCode;
    }

    @Override
    public String toString() {
        return "NewAccountConfirmation{" +
                "user='" + user + '\'' +
                ", email='" + email + '\'' +
                ", confirmationCode='" + confirmationCode + '\'' +
                '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
