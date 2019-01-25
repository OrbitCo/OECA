package gov.epa.oeca.common.domain;

import gov.epa.oeca.common.domain.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author dfladung
 */
@MappedSuperclass
public abstract class BaseEntity implements Entity<BaseEntity> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean sameIdentityAs(BaseEntity other) {
        return id.equals(other.id);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BaseEntity && ((BaseEntity) obj).getId().equals(id));
    }

}
