package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.StringUtils;

/**
 * @author dfladung
 */
public class User extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String userId;
    String password;
    String firstName;
    String lastName;
    String middleInitial;
    String title;
    String suffix;
    String jobTitle;
    Boolean governmentPartner;
    Long verificationIndexElectronic;
    Long verificationIndexPaper;
    String status;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleInitial='" + middleInitial + '\'' +
                ", title='" + title + '\'' +
                ", suffix='" + suffix + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", governmentPartner=" + governmentPartner +
                ", verificationIndexElectronic=" + verificationIndexElectronic +
                ", verificationIndexPaper=" + verificationIndexPaper +
                ", status='" + status + '\'' +
                '}';
    }

    public String getUserId() {
        return StringUtils.upperCase(userId);
    }

    public void setUserId(String userId) {
        this.userId = StringUtils.upperCase(userId);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Boolean getGovernmentPartner() {
        return governmentPartner;
    }

    public void setGovernmentPartner(Boolean governmentPartner) {
        this.governmentPartner = governmentPartner;
    }

    public Long getVerificationIndexElectronic() {
        return verificationIndexElectronic;
    }

    public void setVerificationIndexElectronic(Long verificationIndexElectronic) {
        this.verificationIndexElectronic = verificationIndexElectronic;
    }

    public Long getVerificationIndexPaper() {
        return verificationIndexPaper;
    }

    public void setVerificationIndexPaper(Long verificationIndexPaper) {
        this.verificationIndexPaper = verificationIndexPaper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
