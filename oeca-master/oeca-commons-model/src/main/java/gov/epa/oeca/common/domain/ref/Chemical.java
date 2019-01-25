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
@Table(name = "oeca_ref_chemicals",
        indexes = {@Index(name = "idx_chemicals", columnList = "pollutant_desc")})
public class Chemical extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "pollutant_desc", length = 4000, nullable = false)
    String name;

    @Column(name = "srs_id")
    String srsId;

    @Column(name = "pollutant_category_code")
    String categoryCode;

    @Column(name = "chemical_formula")
    String chemicalFormula;

    @Column(name = "chemical_abstract_service_nmbr")
    String chemicalAbstractServiceNumber;

    @Column(name = "status_flag", length = 1, nullable = false)
    String status;

    @Column(name = "srs_systematic_name", length = 4000)
    String srsSystematicName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSrsId() {
        return srsId;
    }

    public void setSrsId(String srsId) {
        this.srsId = srsId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public void setChemicalFormula(String chemicalFormula) {
        this.chemicalFormula = chemicalFormula;
    }

    public String getChemicalAbstractServiceNumber() {
        return chemicalAbstractServiceNumber;
    }

    public void setChemicalAbstractServiceNumber(String chemicalAbstractServiceNumber) {
        this.chemicalAbstractServiceNumber = chemicalAbstractServiceNumber;
    }

    public String getSrsSystematicName() {
        return srsSystematicName;
    }

    public void setSrsSystematicName(String srsSystematicName) {
        this.srsSystematicName = srsSystematicName;
    }
}
