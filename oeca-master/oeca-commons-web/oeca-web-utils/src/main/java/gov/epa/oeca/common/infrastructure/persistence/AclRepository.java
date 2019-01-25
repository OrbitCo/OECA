package gov.epa.oeca.common.infrastructure.persistence;

import com.google.common.collect.Lists;
import gov.epa.oeca.common.domain.acl.*;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ktucker
 */
@Repository
public class AclRepository {

    @Autowired
    SessionFactory oecaSessionFactory;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    public List<PermissionType> retrieveApplicablePermissions(Module module, EntityType entityType) {
        return (List<PermissionType>) oecaSessionFactory.getCurrentSession()
                .createQuery("select permissionType from ModulePermission " +
                        "where module = :module and entityType = :entityType " +
                        "and permissionType <> 'None' " +
                        "order by permissionType")
                .setParameter("module", module)
                .setParameter("entityType", entityType)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public ModulePermission retrieveModulePermission(Module module, EntityType entityType,
                                                     PermissionType permissionType) {
        List<ModulePermission> results = (List<ModulePermission>) oecaSessionFactory.getCurrentSession()
                .createQuery("from ModulePermission " +
                        "where module = :module and entityType = :entityType and permissionType = :permissionType")
                .setParameter("module", module)
                .setParameter("entityType", entityType)
                .setParameter("permissionType", permissionType)
                .getResultList();
        return (results.size() == 1 ? results.get(0) : null);
    }

    @SuppressWarnings("unchecked")
    public List<UserPermission> retrieveAccess(String userId, UserDomain userDomain, String entityId,
                                               Module module, EntityType entityType, PermissionType permissionType) {
        StringBuilder queryStr = new StringBuilder("select up from UserPermission up " +
                "join up.modulePermission mp " +
                "join up.user u " +
                "where 1 = 1 ");
        if (userId != null && userDomain != null) {
            queryStr.append("and u.userId = :userId and u.userDomain = :userDomain ");
        }
        if (entityId != null) {
            queryStr.append("and up.entityId = :entityId ");
        }
        if (module != null && entityType != null) {
            queryStr.append("and mp.module = :module and mp.entityType = :entityType ");
        }
        if (permissionType != null) {
            queryStr.append("and mp.permissionType = :permissionType ");
        }

        Query query = oecaSessionFactory.getCurrentSession().createQuery(queryStr.toString());
        if (userId != null && userDomain != null) {
            query.setParameter("userId", userId);
            query.setParameter("userDomain", userDomain);
        }
        if (entityId != null) {
            query.setParameter("entityId", entityId);
        }
        if (module != null && entityType != null) {
            query.setParameter("module", module);
            query.setParameter("entityType", entityType);
        }
        if (permissionType != null) {
            query.setParameter("permissionType", permissionType);
        }

        return (List<UserPermission>) query.getResultList();
    }

    public UserPermission saveAccess(UserPermission userPermission) {
        oecaSessionFactory.getCurrentSession().save(userPermission);
        return userPermission;
    }

    public void deleteAccess(UserPermission userPermission) {
        oecaSessionFactory.getCurrentSession().delete(userPermission);
    }

    public UserPermissionRequest retrieveAccessRequest(Long requestId) {
        return oecaSessionFactory.getCurrentSession().get(UserPermissionRequest.class, requestId);
    }

    public List<UserPermissionRequest> retrieveAccessRequests(String userId, UserDomain userDomain, String entityId,
                                                              Module module, EntityType entityType, PermissionType permissionType,
                                                              Boolean pending, NotificationStatus notificationStatus) {
        StringBuilder queryStr = new StringBuilder("select upr from UserPermissionRequest upr " +
                "join upr.modulePermission mp " +
                "join upr.user u " +
                "where 1 = 1 ");
        if (userId != null && userDomain != null) {
            queryStr.append("and u.userId = :userId and u.userDomain = :userDomain ");
        }
        if (entityId != null) {
            queryStr.append("and upr.entityId = :entityId ");
        }
        if (module != null && entityType != null) {
            queryStr.append("and mp.module = :module and mp.entityType = :entityType ");
        }
        if (permissionType != null) {
            queryStr.append("and mp.permissionType = :permissionType ");
        }
        if (notificationStatus != null) {
            queryStr.append("and upr.notificationStatus = :notificationStatus ");
        }

        if (Boolean.TRUE.equals(pending)) {
            queryStr.append("and upr.requestStatus = 'Pending' ");
        }
        else if (Boolean.FALSE.equals(pending)) {
            queryStr.append("and upr.requestStatus <> 'Pending' ");
        }

        Query query = oecaSessionFactory.getCurrentSession().createQuery(queryStr.toString());
        if (userId != null && userDomain != null) {
            query.setParameter("userId", userId);
            query.setParameter("userDomain", userDomain);
        }
        if (entityId != null) {
            query.setParameter("entityId", entityId);
        }
        if (module != null && entityType != null) {
            query.setParameter("module", module);
            query.setParameter("entityType", entityType);
        }
        if (permissionType != null) {
            query.setParameter("permissionType", permissionType);
        }
        if (notificationStatus != null) {
            query.setParameter("notificationStatus", notificationStatus);
        }

        return (List<UserPermissionRequest>) query.getResultList();
    }

    public List<UserPermissionRequest> retrievePendingAccessRequestsForAllEntities(String userId, UserDomain userDomain,
                                                                            Module module, EntityType entityType,
                                                                            PermissionType permissionType) {
        StringBuilder queryStr = new StringBuilder("select upr from UserPermissionRequest upr where " +
                "upr.entityId in (select distinct up.entityId from UserPermission up join up.modulePermission mp " +
                "join up.user u where u.userId = :userId and mp.permissionType = :permissionType and " +
                "u.userDomain = :userDomain and mp.module = :module) and upr.requestStatus = 'Pending' ");
        if(entityType != null) {
            queryStr.append("and upr.modulePermission.entityType = :entityType");
        }
        Query query = oecaSessionFactory.getCurrentSession().createQuery(queryStr.toString());
        query.setParameter("userId", userId)
                .setParameter("userDomain", userDomain)
                .setParameter("module", module)
                .setParameter("permissionType", permissionType);
        if(entityType != null) {
            query.setParameter("entityType", entityType);
        }
        return (List<UserPermissionRequest>) query.getResultList();
    }

    public List<UserPermissionRequest> retrievePendingAccessRequestsForAllEntities(Module module, EntityType entityType) {
        StringBuilder queryStr = new StringBuilder("select r ");
        queryStr.append("from UserPermissionRequest r join r.modulePermission m ");
        queryStr.append("where r.requestStatus = 'Pending' and m.module = :module ");
        if(entityType != null) {
            queryStr.append("and r.modulePermission.entityType = :entityType");
        }
        Query query = oecaSessionFactory.getCurrentSession().createQuery(queryStr.toString());
        query.setParameter("module", module);
        if(entityType != null) {
            query.setParameter("entityType", entityType);
        }
        return (List<UserPermissionRequest>) query.getResultList();
    }

    public List<Object[]> retrievePermissionCountPerEntity(List<String> entityIds, PermissionType permissionType) {
        StringBuilder queryStr = new StringBuilder("select u.entityId, count(*) from UserPermission u join " +
                "u.modulePermission m where m.permissionType = :permissionType and ( ");
        List<List<String>> entityGroups = Lists.partition(entityIds, 1000);
        for(int i = 0; i < entityGroups.size(); ++i) {
            if(i != 0) {
                queryStr.append(" or");
            }
            queryStr.append(" u.entityId in :entityIds");
            queryStr.append(i);
        }
        queryStr.append(") group by u.entityId");
        Query query = oecaSessionFactory.getCurrentSession().createQuery(queryStr.toString());
        query.setParameter("permissionType", permissionType);
        int i = 0;
        for(List<String> group : entityGroups) {
            query.setParameter("entityIds" + i++, group);
        }
        return (List<Object[]>) query.getResultList();
    }

    public UserPermissionRequest saveAccessRequest(UserPermissionRequest request) {
        oecaSessionFactory.getCurrentSession().save(request);
        return request;
    }

    public void deleteAccessRequest(UserPermissionRequest request) {
        oecaSessionFactory.getCurrentSession().delete(request);
    }

    @SuppressWarnings("unchecked")
    public User retrieveUser(String userId, UserDomain userDomain) {
        List<User> results = (List<User>) oecaSessionFactory.getCurrentSession()
                .createQuery("from User " +
                        "where userId = :userId and userDomain = :userDomain")
                .setParameter("userId", userId)
                .setParameter("userDomain", userDomain)
                .getResultList();
        return (results.size() == 1 ? results.get(0) : null);
    }

    public User saveUser(User user) {
        oecaSessionFactory.getCurrentSession().save(user);
        return user;
    }

}
