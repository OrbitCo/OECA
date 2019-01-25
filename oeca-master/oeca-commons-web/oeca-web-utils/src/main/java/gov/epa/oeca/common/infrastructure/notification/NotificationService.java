package gov.epa.oeca.common.infrastructure.notification;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.notification.NewAccountConfirmation;

import java.util.List;

/**
 * @author dfladung
 */
public interface NotificationService {

    void sendNewAccountConfirmationNotification(NewAccountConfirmation confirmation) throws ApplicationException;

    void sendGenericNotification(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
                                 String body) throws ApplicationException;

    void sendNotificationWithAttachments(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
                                        String body, List<Document> attachments) throws ApplicationException;
}
