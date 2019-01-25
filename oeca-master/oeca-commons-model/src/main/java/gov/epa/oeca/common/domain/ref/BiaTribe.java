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
@Table(name = "oeca_ref_bia_tribes",
        indexes = {@Index(name = "idx_bia_tribes", columnList = "state_code")})
public class BiaTribe extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "state_code", nullable = false, length = 2)
    String stateCode;

    @Column(name = "tribe_name", nullable = false, length = 400)
    String tribeName;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getTribeName() {
        return tribeName;
    }

    public void setTribeName(String tribeName) {
        this.tribeName = tribeName;
    }
}
