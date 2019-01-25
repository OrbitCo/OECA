package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;

/**
 * @author dfladung
 */
@Entity
@Table(name = "oeca_ref_pollutants",
        indexes = {@Index(name = "idx_pollutants", columnList = "pollutant_name, pollutant_srs_name")})
public class Pollutant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "pollutant_code", nullable = false)
    Integer pollutantCode;
    @Column(name = "pollutant_name", nullable = false)
    String pollutantName;
    @Column(name = "pollutant_srs_name", length = 4000)
    String srsName;

    public Integer getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(Integer pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public String getPollutantName() {
        return pollutantName;
    }

    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public String getSrsName() {
        return srsName;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }
}
