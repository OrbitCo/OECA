package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oeca_ref_sectors")
public class Sector extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "sector_code", length = 2, nullable = false)
    String sectorCode;

    @Column(name = "sector_name", nullable = false)
    String sectorName;

    @Override
    public String toString() {
        return "Sector{" +
                "sectorCode='" + sectorCode + '\'' +
                ", sectorName='" + sectorName + '\'' +
                "} ";
    }

	public String getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
}
