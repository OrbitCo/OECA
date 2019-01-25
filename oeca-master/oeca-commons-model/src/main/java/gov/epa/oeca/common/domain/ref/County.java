package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author dfladung
 */
@Entity
@Table(name = "oeca_ref_counties",
        indexes = {@Index(name = "idx_counties", columnList = "state_code")})
public class County extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "state_code", length = 2, nullable = false)
    String stateCode;

    @Column(name = "county_name", nullable = false)
    String countyName;

    @Override
    public String toString() {
        return "County{" +
                "stateCode='" + stateCode + '\'' +
                ", countyName='" + countyName + '\'' +
                "} " + super.toString();
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
