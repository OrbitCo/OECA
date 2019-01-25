package gov.epa.oeca.common.domain.acl;

import gov.epa.oeca.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "oeca_acl_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "user_domain", nullable = false)
    @Enumerated(EnumType.STRING)
	UserDomain userDomain;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userDomain='" + userDomain + '\'' +
                "} ";
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

}
