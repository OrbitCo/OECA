package gov.epa.oeca.services.ref.infrastructure.persistence;

import gov.epa.oeca.common.domain.BaseEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

import java.util.List;

/**
 * @author dfladung
 */
public abstract class BaseRepository<T extends BaseEntity> {

    @Autowired
    protected SessionFactory oecaSessionFactory;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected LobHandler lobHandler;

    private Class<T> clazz;

    public BaseRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public T find(Long id) {
        return (T) oecaSessionFactory.getCurrentSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return oecaSessionFactory.getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public Long save(T entity) {
    	oecaSessionFactory.getCurrentSession().save(entity); // save instead of saveOrUpdate: hibernate bug HHH-6776
        return entity.getId();
    }

    public void update(T entity) {
    	oecaSessionFactory.getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
    	oecaSessionFactory.getCurrentSession().delete(entity);
    }
}
