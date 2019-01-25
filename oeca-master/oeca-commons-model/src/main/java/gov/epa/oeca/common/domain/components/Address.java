package gov.epa.oeca.common.domain.components;

import java.util.Objects;

import org.apache.commons.lang.builder.EqualsBuilder;

import gov.epa.oeca.common.domain.BaseValueObject;

public class Address extends BaseValueObject {
    private static final long serialVersionUID = 1L;

    String address1;
    String address2;
    String city;
    String stateCode;
    String zipCode;
    String county;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Address that = (Address) o;
        
        return new EqualsBuilder()
                .append(address1, that.address1)
                .append(address2, that.address2)
                .append(city, that.city)
                .append(stateCode, that.stateCode)
                .append(zipCode, that.zipCode)
                .append(county, that.county)
				.isEquals();
    }

    @Override
    public String toString() {
        return "Address{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", county='" + county + '\'' +
                '}';
    }

	@Override
	public int hashCode() {
		return Objects.hash(address1, address2, city, stateCode, zipCode, county);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address that = (Address) obj;
        return Objects.equals(address1, that.address1)
                && Objects.equals(address2, that.address2)
                && Objects.equals(city, that.city)
                && Objects.equals(stateCode, that.stateCode)
                && Objects.equals(zipCode, that.zipCode)
                && Objects.equals(county, that.county);
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

}
