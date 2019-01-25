package gov.epa.oeca.services.ref.infrastructure.persistence;

import gov.epa.oeca.common.domain.ref.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.NoResultException;

/**
 * @author dfladung
 */
@Repository
public class ReferenceRepository {

    @Autowired
    SessionFactory oecaSessionFactory;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    public List<State> retrieveStates() {
        return (List<State>) oecaSessionFactory.getCurrentSession().createQuery("from State where stateUsage = 'B' order by stateName").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<State> retrieveStates(Integer region) {
        String regionCode = region < 10 ? "0" + region.toString() : region.toString();
        return (List<State>) oecaSessionFactory.getCurrentSession()
                .createQuery("from State where stateUsage = 'B' and regionCode = ? order by stateName")
                .setParameter(0, regionCode)
                .getResultList();
    }

    public State retrieveState(String stateCode) {
        return (State) oecaSessionFactory.getCurrentSession()
                .createQuery("from State where stateCode = ?")
                .setParameter(0, stateCode)
                .getSingleResult();

    }

    @SuppressWarnings("unchecked")
    public List<County> retrieveCounties() {
        return (List<County>) oecaSessionFactory.getCurrentSession().createQuery("from County order by countyName").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Tribe> retrieveTribes() {
        return (List<Tribe>) oecaSessionFactory.getCurrentSession().createQuery("from Tribe").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Tribe> retrieveTribes(String stateCode) {
        return (List<Tribe>) oecaSessionFactory.getCurrentSession()
                .createQuery("select t from Tribe t join t.states ts where ts.stateCode = ?")
                .setParameter(0, stateCode)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Tribe> retrieveTribesByLandName(String tribalLandName) {
        return (List<Tribe>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Tribe t where t.tribalName = ?")
                .setParameter(0, tribalLandName)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public Tribe retrieveTribeByLandNameAndStateCode(String tribalLandName, String stateCode) {
        try {
	    	return (Tribe) oecaSessionFactory.getCurrentSession()
	                .createQuery("select t from Tribe t join t.states ts where t.tribalName = ? and ts.stateCode = ?")
	                .setParameter(0, tribalLandName)
	                .setParameter(1, stateCode)
	                .getSingleResult();
        }
        catch (NoResultException nre) {
        	return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<BiaTribe> retrieveBiaTribes() {
        return (List<BiaTribe>) oecaSessionFactory.getCurrentSession()
                .createQuery("from BiaTribe")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<BiaTribe> retrieveBiaTribes(String stateCode) {
        return (List<BiaTribe>) oecaSessionFactory.getCurrentSession()
                .createQuery("from BiaTribe where stateCode = ?")
                .setParameter(0, stateCode)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<County> retrieveCounties(String stateCode) {
        return (List<County>) oecaSessionFactory.getCurrentSession()
                .createQuery("from County where stateCode = ?")
                .setParameter(0, stateCode)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Chemical> retrieveActiveChemicals() {
        return (List<Chemical>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Chemical where status = 'A'")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Chemical> retrieveActiveChemicals(String name) {
        return (List<Chemical>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Chemical where status = 'A' and lower(name) like lower(:chemName)")
                .setParameter("chemName", "%" + name + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Pollutant> retrievePollutants(String name) {
        return (List<Pollutant>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Pollutant where lower(pollutantName) like lower(:pollutantName) or lower(srsName) like lower(:srsName)")
                .setParameter("pollutantName", "%" + name.toLowerCase() + "%")
                .setParameter("srsName", "%" + name.toLowerCase() + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Sector> retrieveSectors() {
        return (List<Sector>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Sector")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Subsector> retrieveSubsectors() {
        return (List<Subsector>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Subsector")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Subsector> retrieveSubsectorsBySectorCode(String sectorCode) {
        return (List<Subsector>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Subsector where sectorCode = ?")
                .setParameter(0, sectorCode)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Sic> retrieveSics() {
        return (List<Sic>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Sic")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Sic> retrieveSicsBySubsectorCode(String subsectorCode) {
        return (List<Sic>) oecaSessionFactory.getCurrentSession()
                .createQuery("from Sic where subsectorCode = ?")
                .setParameter(0, subsectorCode)
                .getResultList();
    }

}
