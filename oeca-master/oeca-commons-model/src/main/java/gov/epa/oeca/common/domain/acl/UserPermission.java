package gov.epa.oeca.common.domain.acl;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "oeca_acl_user_permissions")
public class UserPermission extends UserPermissionBase {

    private static final long serialVersionUID = 1L;

}
