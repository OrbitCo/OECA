package gov.epa.oeca.common.infrastructure.node2;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.Transaction;
import gov.epa.oeca.common.domain.node.TransactionStatus;
import gov.epa.oeca.common.infrastructure.cdx.signature.DocumentDataSource;
import gov.epa.oeca.common.infrastructure.soap.AbstractClient;
import net.exchangenetwork.schema.node._2.AttachmentType;
import net.exchangenetwork.schema.node._2.DocumentFormatType;
import net.exchangenetwork.schema.node._2.NodeDocumentType;
import net.exchangenetwork.schema.node._2.TransactionStatusCode;
import net.exchangenetwork.wsdl.node._2.NetworkNodePortType2;
import net.exchangenetwork.wsdl.node._2.NodeFaultMessage;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.activation.DataHandler;
import javax.xml.ws.Holder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Component
public class NetworkNode2Client extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(NetworkNode2Client.class);

    protected NetworkNodePortType2 getClient(URL endpoint, boolean mtom, boolean chunking) {
        return (NetworkNodePortType2) this.getClient(endpoint.toString(), NetworkNodePortType2.class, mtom, chunking);
    }

    public String authenticate(URL endpoint, String userId, String credential, String domain,
                               String authenticationMethod) {
        try {
            return getClient(endpoint, false, true).authenticate(userId, credential, domain, authenticationMethod);
        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        }
    }

    public List<Document> download(URL endpoint, String token, String transactionId, String dataflow) {
        try {
            Holder<List<NodeDocumentType>> docs = new Holder<List<NodeDocumentType>>();
            getClient(endpoint, false, true).download(token, dataflow, transactionId, docs);
            return convertDocumentsFromNode(docs.value);
        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        }
    }

    public Transaction getStatus(URL endpoint, String token, String transactionId) {
        Holder<String> tx = new Holder<String>(transactionId);
        Holder<TransactionStatusCode> status = new Holder<TransactionStatusCode>();
        Holder<String> statusDetail = new Holder<String>();
        try {
            getClient(endpoint, false, true).getStatus(token, tx, status, statusDetail);
            return new Transaction(tx.value, TransactionStatus.fromValue(status.value.value()), statusDetail.value);
        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        }
    }

    public String submit(URL endpoint, String token, String transactionId, String dataflow, String operation,
                         List<Document> documents) {
        Holder<String> tx = new Holder<String>(transactionId);
        Holder<TransactionStatusCode> status = new Holder<TransactionStatusCode>();
        Holder<String> statusDetail = new Holder<String>();
        try {
            List<NodeDocumentType> nodeDocuments = convertDocumentsToNode(documents);
            String op = (operation == null) ? "" : operation;
            getClient(endpoint, false, true).submit(token, tx, dataflow, op, null, null, nodeDocuments, status,
                    statusDetail);
            return tx.value;
        } catch (NodeFaultMessage fault) {
            throw this.handleException(convertFault(fault), logger);
        }
    }

    private List<NodeDocumentType> convertDocumentsToNode(List<Document> documents) {
        List<NodeDocumentType> nodeDocuments = new ArrayList<NodeDocumentType>();
        if (documents != null) {
            for (Iterator<Document> i = documents.iterator(); i.hasNext(); ) {
                Document document = i.next();
                NodeDocumentType nodeDocument = new NodeDocumentType();
                AttachmentType attachment = new AttachmentType();

                if (document.getName().toLowerCase().endsWith(".xml")) {
                    nodeDocument.setDocumentFormat(DocumentFormatType.XML);
                    attachment.setValue(new DataHandler(new DocumentDataSource(document.getContent(),
                            "text/xml")));
                }
                else if (document.getName().toLowerCase().endsWith(".txt")) {
                    nodeDocument.setDocumentFormat(DocumentFormatType.FLAT);
                    attachment.setValue(new DataHandler(new DocumentDataSource(document.getContent(),
                            "text/plain")));
                }
                else if (document.getName().toLowerCase().endsWith(".zip")) {
                    nodeDocument.setDocumentFormat(DocumentFormatType.ZIP);
                    attachment.setValue(new DataHandler(new DocumentDataSource(document.getContent(),
                            "application/octet-stream")));
                }
                else {
                    nodeDocument.setDocumentFormat(DocumentFormatType.BIN);
                    attachment.setValue(new DataHandler(new DocumentDataSource(document.getContent(),
                            "application/octet-stream")));
                }

                nodeDocument.setDocumentName(document.getName());
                attachment.setContentType(attachment.getValue().getContentType());
                nodeDocument.setDocumentContent(attachment);

                nodeDocuments.add(nodeDocument);
            }
        }
        return nodeDocuments;
    }

    private List<Document> convertDocumentsFromNode(List<NodeDocumentType> nodeDocuments) {
        List<Document> documents = new ArrayList<Document>();
        if (nodeDocuments != null) {
            for (Iterator<NodeDocumentType> i = nodeDocuments.iterator(); i.hasNext(); ) {
                NodeDocumentType nodeDocument = i.next();
                Document document = new Document();
                document.setName(nodeDocument.getDocumentName());
                document.setContent(getNodeDocumentContent(nodeDocument));
                documents.add(document);
            }
        }
        return documents;
    }

    private File getNodeDocumentContent(NodeDocumentType nodeDocument) {
        FileOutputStream out = null;
        InputStream content = null;
        try {
            String fileName = UUID.randomUUID().toString();
            File file = File.createTempFile(fileName, "." + nodeDocument.getDocumentFormat().value().toLowerCase());
            out = new FileOutputStream(file);
            content = nodeDocument.getDocumentContent().getValue().getInputStream();
            FileCopyUtils.copy(content, out);
            return file;
        } catch (Exception e) {
            logger.error("Could not get content from node document.", e);
            return null;
        } finally {
            IOUtils.closeQuietly(content);
            IOUtils.closeQuietly(out);
        }
    }

    private ApplicationException convertFault(NodeFaultMessage fault) {
        if (fault.getFaultInfo() == null) {
            return new ApplicationException(ApplicationErrorCode.E_RemoteServiceError, fault.getMessage());
        } else {
            ApplicationErrorCode code;
            try {
                code = ApplicationErrorCode.valueOf(fault.getFaultInfo().getErrorCode().value());
            } catch (Exception e) {
                logger.warn("Could not translate fault.");
                code = ApplicationErrorCode.E_RemoteServiceError;
            }
            return new ApplicationException(code, fault.getFaultInfo().getDescription());
        }
    }
}
