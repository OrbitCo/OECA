package gov.epa.oeca.common.domain.components;

import gov.epa.oeca.common.domain.components.Contact;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Operator extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    protected String operatorName;
    protected Boolean operatorFederal;
    protected Address operatorAddress;
    protected Contact operatorPointOfContact;
    protected Contact preparer;
    protected Contact certifier;

    public Operator() {
    	operatorAddress = new Address();
    	operatorPointOfContact = new Contact();
        preparer = new Contact();
        certifier = new Contact();
    }

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Operator that = (Operator) o;

        return new EqualsBuilder()
                .append(operatorName, that.operatorName)
                .append(operatorFederal, that.operatorFederal)
                .append(operatorAddress, that.operatorAddress)
                .append(operatorPointOfContact, that.operatorPointOfContact)
                .append(preparer, that.preparer)
                .append(certifier, that.certifier)
                .isEquals();
    }


    @Override
    public String toString() {
        return "OperatorInformation{" +
                "operatorName='" + operatorName + '\'' +
                ", operatorFederal=" + operatorFederal +
                ", operatorAddress=" + operatorAddress +
                ", operatorPointOfContact=" + operatorPointOfContact +
                ", preparer=" + preparer +
                ", certifier=" + certifier +
                "} ";
    }

    public Contact getCertifier() {
        return certifier;
    }

    public void setCertifier(Contact certifier) {
        this.certifier = certifier;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Boolean getOperatorFederal() {
        return operatorFederal;
    }

    public void setOperatorFederal(Boolean operatorFederal) {
        this.operatorFederal = operatorFederal;
    }

    public Address getOperatorAddress() {
        return operatorAddress;
    }

    public void setOperatorAddress(Address operatorAddress) {
        this.operatorAddress = operatorAddress;
    }

    public Contact getOperatorPointOfContact() {
        return operatorPointOfContact;
    }

    public void setOperatorPointOfContact(Contact operatorPointOfContact) {
        this.operatorPointOfContact = operatorPointOfContact;
    }

    public Contact getPreparer() {
        return preparer;
    }

    public void setPreparer(Contact preparer) {
        this.preparer = preparer;
    }

}
