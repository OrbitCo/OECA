package gov.epa.oeca.common.domain.registration;

import gov.epa.oeca.common.domain.BaseValueObject;

import java.util.Comparator;

import static java.lang.Math.toIntExact;

/**
 * @author dfladung
 */
public class Role extends BaseValueObject implements Comparable<Role> {

    private static final long serialVersionUID = 1L;

    Long code;
    Long userRoleId;
    String dataflow;
    String description;
    String status;
    String esaRequirement;
    Boolean signatureQuestionsRequired;
    String subject;

    @Override
    public String toString() {
        return "Role{" +
                "code=" + code +
                ", userRoleId=" + userRoleId +
                ", dataflow='" + dataflow + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", esaRequirement='" + esaRequirement + '\'' +
                ", signatureQuestionsRequired=" + signatureQuestionsRequired +
                ", subject='" + subject + '\'' +
                '}';
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getDataflow() {
        return dataflow;
    }

    public void setDataflow(String dataflow) {
        this.dataflow = dataflow;
    }

    public String getEsaRequirement() {
        return esaRequirement;
    }

    public void setEsaRequirement(String esaRequirement) {
        this.esaRequirement = esaRequirement;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSignatureQuestionsRequired() {
        return signatureQuestionsRequired;
    }

    public void setSignatureQuestionsRequired(Boolean signatureQuestionsRequired) {
        this.signatureQuestionsRequired = signatureQuestionsRequired;
    }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public int compareTo(Role compareRole) {
        Long compareCode = ((Role) compareRole).getCode();
        //ascending order
        return toIntExact(this.code - compareCode);

    }
}
