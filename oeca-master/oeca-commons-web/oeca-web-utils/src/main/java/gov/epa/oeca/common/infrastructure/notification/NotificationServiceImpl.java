package gov.epa.oeca.common.infrastructure.notification;

import freemarker.template.Configuration;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.OecaConstants;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.notification.NewAccountConfirmation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author dfladung
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);


    @Autowired
    JavaMailSender mailSender;
    @Autowired
    Configuration freemarkerConfiguration;
    @Resource(name = "additionalMailConfiguration")
    Map<String, String> additionalMailConfiguration;

    @Override
    public void sendNewAccountConfirmationNotification(NewAccountConfirmation confirmation) throws ApplicationException {
        try {
            // validate input
            Validate.notNull(confirmation, "Confirmation is required.");
            Validate.notEmpty(confirmation.getEmail(), "Confirmation email address is required.");
            Validate.isTrue(
                    confirmation.getEmail().matches(OecaConstants.VALID_EMAIL_REGEX),
                    String.format("%s is not a valid email address.", confirmation.getEmail()));
            Validate.notEmpty(confirmation.getConfirmationCode(), "Confirmation code is required.");
            Validate.notEmpty(confirmation.getUser(), "Confirmation user is required.");

            // get the info
            String to = confirmation.getEmail();
            String from = additionalMailConfiguration.get("CdxHelpDeskEmail");
            Map<String, Object> subjectModel = new HashMap<>();
            subjectModel.put("environment", additionalMailConfiguration.get("environment"));
            String subject = mergeTemplate("notifications/new-account/confirmation-subject.fm", subjectModel);
            Map<String, Object> bodyModel = new HashMap<>();
            bodyModel.put("ConfirmationCode", confirmation.getConfirmationCode());
            bodyModel.putAll(additionalMailConfiguration);
            String body = mergeTemplate("notifications/new-account/confirmation-body.fm", bodyModel);

            // send it!
            mailSender.send(getMessagePreparator(from, Collections.singletonList(to), null, null, subject, body, null));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (MailException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Messaging, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void sendGenericNotification(String from, List<String> to, List<String> cc, List<String> bcc,
                                        String subject, String body) throws ApplicationException {
        if (System.getProperties().containsKey("suppressNotifications")
                && BooleanUtils.toBoolean(System.getProperties().get("suppressNotifications").toString())) {
            logger.warn("suppressing notifications due to configuration");
            return;
        }
        try {
            // validate input
            Validate.notEmpty(from, "From address is required.");
            Validate.notEmpty(to, "At least one to address is required");
            Validate.notEmpty(subject, "Subject is required.");
            Validate.notEmpty(body, "Body is required.");
            // send it!
            mailSender.send(getMessagePreparator(from, getUniqueEmails(to), getUniqueEmails(cc), getUniqueEmails(bcc),
                    subject, body, null));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (MailException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Messaging, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void sendNotificationWithAttachments(String from, List<String> to, List<String> cc, List<String> bcc,
                                        String subject, String body, List<Document> attachments) throws ApplicationException {
        if (System.getProperties().containsKey("suppressNotifications")
                && BooleanUtils.toBoolean(System.getProperties().get("suppressNotifications").toString())) {
            logger.warn("suppressing notifications due to configuration");
            return;
        }
        try {
            // validate input
            Validate.notEmpty(from, "From address is required.");
            Validate.notEmpty(to, "At least one to address is required");
            Validate.notEmpty(subject, "Subject is required.");
            Validate.notEmpty(body, "Body is required.");
            Validate.notEmpty(attachments, "At least one attachment is required.");
            // send it!
            mailSender.send(getMessagePreparator(from, getUniqueEmails(to), getUniqueEmails(cc), getUniqueEmails(bcc),
                    subject, body, attachments));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (MailException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Messaging, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    MimeMessagePreparator getMessagePreparator(final String from, final List<String> to, final List<String> cc,
                                               final List<String> bcc, final String subject, final String body,
                                               List<Document> attachments) {
        return mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to.toArray(new String[to.size()]));
            if (CollectionUtils.isNotEmpty(cc)) {
                helper.setCc(cc.toArray(new String[cc.size()]));
            }
            if (CollectionUtils.isNotEmpty(bcc)) {
                helper.setBcc(bcc.toArray(new String[bcc.size()]));
            }
            helper.setSubject(subject);
            helper.setText(body, true);
            if (CollectionUtils.isNotEmpty(attachments)) {
                for (Document d : attachments) {
                    helper.addAttachment(d.getName(), d.getContent());
                }
            }
        };
    }

    String mergeTemplate(String template, Map<String, Object> model) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(template), model);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Messaging, e.getMessage());
        }
    }

    List<String> getUniqueEmails(List<String> emails) {
        return CollectionUtils.isEmpty(emails) ? null : new ArrayList<>(new HashSet<>(emails));
    }
}
