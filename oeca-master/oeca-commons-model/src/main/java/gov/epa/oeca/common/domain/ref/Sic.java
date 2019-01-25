package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "oeca_ref_sics",
       indexes = {@Index(name = "idx_sic_scode", columnList = "sector_code"), @Index(name = "idx_sic_sscode", columnList = "subsector_code")})
public class Sic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "sector_code", length = 2, nullable = false)
    String sectorCode;

    @Column(name = "subsector_code", length = 3, nullable = false)
    String subsectorCode;

    @Column(name = "sic_code", length = 4, nullable = false)
    String sicCode;

    @Column(name = "sic_name", nullable = false)
    String sicName;

    @Override
    public String toString() {
        return "Sic{" +
                "sectorCode='" + sectorCode + '\'' +
                ", subsectorCode='" + subsectorCode + '\'' +
                ", sicCode='" + sicCode + '\'' +
                ", sicName='" + sicName + '\'' +
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

	public String getSicCode() {
		return sicCode;
	}

	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}

	public String getSicName() {
		return sicName;
	}

	public void setSicName(String sicName) {
		this.sicName = sicName;
	}
}
