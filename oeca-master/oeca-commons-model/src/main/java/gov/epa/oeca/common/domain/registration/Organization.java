package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

/**
 * @author dfladung
 */
public class Organization extends BaseValueObject {

    static final long serialVersionUID = 1L;

    Long organizationId;
    Long userOrganizationId;
    String organizationName;

    String mailingAddress1;
    String mailingAddress2;
    String mailingAddress3;
    String mailingAddress4;
    String city;
    String stateCode;
    String zip;
    String countryCode;


    String email;
    String phone;
    String phoneExtension;
    String fax;

    Boolean primaryOrg;

    String cdxEsaStatus;

    @Override
    public String toString() {
        return "Organization{" +
                "organizationId=" + organizationId +
                ", userOrganizationId=" + userOrganizationId +
                ", organizationName='" + organizationName + '\'' +
                ", mailingAddress1='" + mailingAddress1 + '\'' +
                ", mailingAddress2='" + mailingAddress2 + '\'' +
                ", mailingAddress3='" + mailingAddress3 + '\'' +
                ", mailingAddress4='" + mailingAddress4 + '\'' +
                ", city='" + city + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", zip='" + zip + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneExtension='" + phoneExtension + '\'' +
                ", fax='" + fax + '\'' +
                ", primaryOrg=" + primaryOrg +
                ", cdxEsaStatus='" + cdxEsaStatus + '\'' +
                '}';
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserOrganizationId() {
        return userOrganizationId;
    }

    public void setUserOrganizationId(Long userOrganizationId) {
        this.userOrganizationId = userOrganizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public String getMailingAddress3() {
        return mailingAddress3;
    }

    public void setMailingAddress3(String mailingAddress3) {
        this.mailingAddress3 = mailingAddress3;
    }

    public String getMailingAddress4() {
        return mailingAddress4;
    }

    public void setMailingAddress4(String mailingAddress4) {
        this.mailingAddress4 = mailingAddress4;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Boolean getPrimaryOrg() {
        return primaryOrg;
    }

    public void setPrimaryOrg(Boolean primaryOrg) {
        this.primaryOrg = primaryOrg;
    }

    public String getCdxEsaStatus() {
        return cdxEsaStatus;
    }

    public void setCdxEsaStatus(String cdxEsaStatus) {
        this.cdxEsaStatus = cdxEsaStatus;
    }
}
