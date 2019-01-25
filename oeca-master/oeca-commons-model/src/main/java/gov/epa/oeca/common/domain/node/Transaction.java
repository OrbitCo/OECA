package gov.epa.oeca.common.domain.node;

import gov.epa.oeca.common.domain.BaseValueObject;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
public class Transaction extends BaseValueObject {

    String txId;
    TransactionStatus status;
    String statusDetail;

    public Transaction() {
    }

    public Transaction(String txId, TransactionStatus status, String statusDetail) {
        this.txId = txId;
        this.status = status;
        this.statusDetail = statusDetail;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "txId='" + txId + '\'' +
                ", status='" + status + '\'' +
                ", statusDetail='" + statusDetail + '\'' +
                '}';
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }
}
