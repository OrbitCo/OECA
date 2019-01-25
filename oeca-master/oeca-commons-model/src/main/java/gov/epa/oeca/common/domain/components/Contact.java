package gov.epa.oeca.common.domain.components;

import java.util.Objects;

import org.apache.commons.lang.builder.EqualsBuilder;

import gov.epa.oeca.common.domain.BaseValueObject;

public class Contact extends BaseValueObject {
    private static final long serialVersionUID = 1L;

    String userId;
    String firstName;
    String lastName;
    String middleInitial;
    String organization;
    String phone;
    String phoneExtension;
    String email;
    String title;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Contact that = (Contact) o;
        
        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(middleInitial, that.middleInitial)
                .append(organization, that.organization)
                .append(phone, that.phone)
                .append(phoneExtension, that.phoneExtension)
                .append(email, that.email)
                .append(title, that.title)
				.isEquals();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleInitial='" + middleInitial + '\'' +
                ", organization='" + organization + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneExtension='" + phoneExtension + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

	@Override
	public int hashCode() {
		return Objects.hash(userId, firstName, lastName, middleInitial, organization, phone, phoneExtension, email, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		return Objects.equals(userId, other.userId) 
				&& Objects.equals(firstName, other.firstName) 
				&& Objects.equals(lastName, other.lastName)
				&& Objects.equals(middleInitial, other.middleInitial) 
				&& Objects.equals(organization, other.organization)
				&& Objects.equals(phone, other.phone) 
				&& Objects.equals(phoneExtension, other.phoneExtension)								
				&& Objects.equals(email, other.email)
				&& Objects.equals(title, other.title);
	}
    
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
