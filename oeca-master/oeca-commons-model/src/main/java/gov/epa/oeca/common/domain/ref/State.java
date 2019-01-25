package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @author dfladung
 */
@Entity
@Table(name = "oeca_ref_states",
        indexes = {@Index(name = "idx_state_usage", columnList = "state_usage")})
public class State extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "region_code", length = 2)
    String regionCode;

    @Column(name = "state_code", length = 2, nullable = false)
    String stateCode;

    @Column(name = "state_name", nullable = false)
    String stateName;

    @Column(name = "state_usage", length = 1)
    String stateUsage;

    @Column(name = "state_fed_operator_text", length = 4000)
    String stateFedOperatorText;

    @JsonIgnore
    @ManyToMany(mappedBy = "states")
    List<Tribe> tribes;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateUsage() {
        return stateUsage;
    }

    public void setStateUsage(String stateUsage) {
        this.stateUsage = stateUsage;
    }

    public String getStateFedOperatorText() { return stateFedOperatorText; }

    public void setStateFedOperatorText(String stateFedOperatorText) {
        this.stateFedOperatorText = stateFedOperatorText;
    }

    public List<Tribe> getTribes() {
        return tribes;
    }

    public void setTribes(List<Tribe> tribes) {
        this.tribes = tribes;
    }
}
