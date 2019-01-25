package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "oeca_ref_subsectors",
       indexes = {@Index(name = "idx_ss_scode", columnList = "sector_code")})
public class Subsector extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "sector_code", length = 2, nullable = false)
    String sectorCode;

    @Column(name = "subsector_code", length = 3, nullable = false)
    String subsectorCode;

    @Column(name = "subsector_name", length = 300, nullable = false)
    String subsectorName;

    @Override
    public String toString() {
        return "Subsector{" +
                "sectorCode='" + sectorCode + '\'' +
                ", subsectorCode='" + subsectorCode + '\'' +
                ", subsectorName='" + subsectorName + '\'' +
                "} ";
    }

	public String getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}

	public String getSubsectorCode() {
		return subsectorCode;
	}

	public void setSubsectorCode(String subsectorCode) {
		this.subsectorCode = subsectorCode;
	}

	public String getSubsectorName() {
		return subsectorName;
	}

	public void setSubsectorName(String subsectorName) {
		this.subsectorName = subsectorName;
	}
}
