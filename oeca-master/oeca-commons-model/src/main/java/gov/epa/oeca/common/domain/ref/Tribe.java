package gov.epa.oeca.common.domain.ref;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author dfladung
 */
@Entity
@Table(name = "oeca_ref_tribes",
        indexes = {@Index(name = "idx_tribes", columnList = "tribal_code")})
public class Tribe extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "tribal_code", nullable = false)
    String tribalCode;

    @Column(name = "tribal_name", nullable = false)
    String tribalName;

    @ManyToMany
    @JoinTable(
            name = "oeca_ref_tribe_states",
            joinColumns = @JoinColumn(name = "tribal_code", referencedColumnName = "tribal_code"),
            inverseJoinColumns = @JoinColumn(name = "state_code", referencedColumnName = "state_code"),
            uniqueConstraints = {@UniqueConstraint(name = "idx_tribal_state", columnNames = {"tribal_code", "state_code"})})
    List<State> states;

    public String getTribalCode() {
        return tribalCode;
    }

    public void setTribalCode(String tribalCode) {
        this.tribalCode = tribalCode;
    }

    public String getTribalName() {
        return tribalName;
    }

    public void setTribalName(String tribalName) {
        this.tribalName = tribalName;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
}
