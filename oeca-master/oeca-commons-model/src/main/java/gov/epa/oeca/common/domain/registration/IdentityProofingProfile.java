package gov.epa.oeca.common.domain.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;


/**
 * @author dfladung
 */
public class IdentityProofingProfile extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String userId;
    String firstName;
    String lastName;
    String middleInitial;
    String mailingAddress1;
    String mailingAddress2;
    String city;
    String state;
    String zip;
    String phone;
    String ssnLast4;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate dateOfBirth;
    long userRoleId;

    public String getUserId() {
        return StringUtils.upperCase(userId);
    }

    public void setUserId(String userId) {
        this.userId = StringUtils.upperCase(userId);
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

    public String getMailingAddress1() {
        return mailingAddress1;
    }

    public void setMailingAddress1(String mailingAddress1) {
        this.mailingAddress1 = mailingAddress1;
    }

    public String getMailingAddress2() {
        return mailingAddress2;
    }

    public void setMailingAddress2(String mailingAddress2) {
        this.mailingAddress2 = mailingAddress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSSNLast4() {
        return ssnLast4;
    }

    public void setSSNLast4(String ssnLast4) {
        this.ssnLast4 = ssnLast4;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }
}
