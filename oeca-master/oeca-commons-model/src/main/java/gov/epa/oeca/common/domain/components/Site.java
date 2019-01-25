package gov.epa.oeca.common.domain.components;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author dfladung
 */
public class Site extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    protected String siteName;
    protected Address siteAddress;
    protected Location siteLocation;
    protected Boolean siteIndianCountry;
    protected String siteIndianCountryTribe;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Site that = (Site) o;

        return new EqualsBuilder()
                .append(siteName, that.siteName)
                .append(siteAddress, that.siteAddress)
                .append(siteLocation, that.siteLocation)
                .append(siteIndianCountry, that.siteIndianCountry)
                .append(siteIndianCountryTribe, that.siteIndianCountryTribe)
                .isEquals();
    }

    @Override
    public String toString() {
        return "SiteInformation{" +
                "siteName='" + siteName + '\'' +
                ", siteAddress=" + siteAddress +
                ", siteLocation=" + siteLocation +
                ", siteIndianCountry=" + siteIndianCountry +
                ", siteIndianCountryTribe='" + siteIndianCountryTribe + '\'' +
                "} ";
    }

    public Location getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(Location siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Address getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(Address siteAddress) {
        this.siteAddress = siteAddress;
    }

    public Boolean getSiteIndianCountry() {
        return siteIndianCountry;
    }

    public void setSiteIndianCountry(Boolean siteIndianCountry) {
        this.siteIndianCountry = siteIndianCountry;
    }

    public String getSiteIndianCountryTribe() {
        return siteIndianCountryTribe;
    }

    public void setSiteIndianCountryTribe(String siteIndianCountryTribe) {
        this.siteIndianCountryTribe = siteIndianCountryTribe;
    }
}
