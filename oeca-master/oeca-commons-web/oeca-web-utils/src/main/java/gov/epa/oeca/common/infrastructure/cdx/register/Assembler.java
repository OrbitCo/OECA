package gov.epa.oeca.common.infrastructure.cdx.register;

import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.registration.*;
import gov.epa.oeca.common.infrastructure.cdx.signature.DocumentDataSource;
import net.exchangenetwork.wsdl.register.idp._1.IdentityProofingUserType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentFormatType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureUserType;
import net.exchangenetwork.wsdl.register.streamlined._1.*;
import net.exchangenetwork.wsdl.register.inbox._1.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.xml.datatype.DatatypeFactory;
import java.io.File;
import java.time.ZoneId;
import java.util.*;

/**
 * @author dfladung
 */
@Component
public class Assembler {

    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    public List<Dataflow> assembleDataflows(List<RegistrationDataflow> args) {
        List<Dataflow> result = new ArrayList<>();
        for (RegistrationDataflow external : args) {
            Dataflow domain = new Dataflow();
            BeanUtils.copyProperties(external, domain);
            result.add(domain);
        }
        Collections.sort(result, Dataflow.DataflowNameComparator);
        return result;
    }

    public List<Role> assembleRoles(List<RegistrationRoleType> args) {
        List<Role> result = new ArrayList<>();
        for (RegistrationRoleType external : args) {
            Role domain = new Role();
            BeanUtils.copyProperties(external, domain);
            domain.setSignatureQuestionsRequired(external.isSignatureQuestionsRequired());
            result.add(domain);
        }
        return result;
    }

    public RegistrationRole assembleRole(Role role) {
        RegistrationRole result = new RegistrationRole();
        BeanUtils.copyProperties(role, result);
        RegistrationRoleType type = new RegistrationRoleType();
        BeanUtils.copyProperties(role, type);
        type.setSignatureQuestionsRequired(role.getSignatureQuestionsRequired());
        result.setType(type);
        return result;
    }

    public Role assembleRole(RegistrationRole role) {
        Role result = new Role();
        BeanUtils.copyProperties(role, result);
        if (role.getStatus() != null) {
            result.setStatus(role.getStatus().getCode());
        }
        if (role.getType() != null) {
            result.setCode(role.getType().getCode());
            result.setSignatureQuestionsRequired(role.getType().isSignatureQuestionsRequired());
            result.setEsaRequirement(role.getType().getEsaRequirement());
        }
        return result;
    }

    public RegistrationOrganization assembleOrg(Organization org) {
        RegistrationOrganization result = new RegistrationOrganization();
        BeanUtils.copyProperties(org, result);
        RegistrationState state = new RegistrationState();
        state.setCode(org.getStateCode());
        result.setState(state);
        RegistrationCountry country = new RegistrationCountry();
        country.setCode(org.getCountryCode());
        result.setCountry(country);
        return result;
    }

    public Organization assembleOrg(RegistrationOrganization org) {
        Organization result = new Organization();
        BeanUtils.copyProperties(org, result);
        if (org.getState() != null) {
            result.setStateCode(org.getState().getCode());
        }
        if (org.getCountry() != null) {
            result.setCountryCode(org.getCountry().getCode());
        }
        return result;
    }

    public List<Organization> assembleDomainOrganizations(List<RegistrationOrganization> organizations) {
        List<Organization> result = new ArrayList<>();
        for (RegistrationOrganization org : organizations) {
            result.add(assembleOrg(org));
        }
        return result;
    }

    public RegistrationAnswer assembleAnswer(Answer answer) {
        RegistrationAnswer result = new RegistrationAnswer();
        BeanUtils.copyProperties(answer, result);
        RegistrationQuestion question = new RegistrationQuestion();
        BeanUtils.copyProperties(answer.getQuestion(), question);
        result.setQuestion(question);
        return result;
    }

    public Answer assembleAnswer(RegistrationAnswer answer) {
        Answer result = new Answer();
        BeanUtils.copyProperties(answer, result);
        Question question = new Question();
        BeanUtils.copyProperties(answer.getQuestion(), question);
        result.setQuestion(question);
        return result;
    }

    public List<RegistrationAnswer> assembleCdxAnswers(List<Answer> answers) {
        List<RegistrationAnswer> result = new ArrayList<>();
        for (Answer answer : answers) {
            result.add(assembleAnswer(answer));
        }
        return result;
    }

    public List<Answer> assembleDomainAnswers(List<RegistrationAnswer> answers) {
        List<Answer> result = new ArrayList<>();
        for (RegistrationAnswer answer : answers) {
            result.add(assembleAnswer(answer));
        }
        return result;
    }

    public RegistrationUser assembleUser(User user) {
        RegistrationUser result = new RegistrationUser();
        BeanUtils.copyProperties(user, result);
        if (!StringUtils.isEmpty(user.getTitle())) {
            RegistrationUserTitle title = new RegistrationUserTitle();
            title.setDescription(user.getTitle());
            result.setTitle(title);
        }
        if (!StringUtils.isEmpty(user.getSuffix())) {
            RegistrationUserSuffix suffix = new RegistrationUserSuffix();
            suffix.setDescription(user.getSuffix());
            result.setSuffix(suffix);
        }
        return result;
    }

    public User assembleUser(RegistrationUser user) {
        User result = new User();
        BeanUtils.copyProperties(user, result);
        if (user.getTitle() != null) {
            result.setTitle(user.getTitle().getDescription());
        }
        if (user.getSuffix() != null) {
            result.setSuffix(user.getSuffix().getDescription());
        }
        return result;
    }

    public RegistrationNewUserProfile assembleNewUserProfile(NewUserProfile newUserProfile) {
        RegistrationNewUserProfile result = new RegistrationNewUserProfile();
        result.setUser(this.assembleUser(newUserProfile.getUser()));
        result.setOrganization(this.assembleOrg(newUserProfile.getOrganization()));
        result.setRole(this.assembleRole(newUserProfile.getRole()));
        if (!CollectionUtils.isEmpty(newUserProfile.getSecretAnswers())) {
            result.getSecretAnswers().addAll(this.assembleCdxAnswers(newUserProfile.getSecretAnswers()));
        }
        if (!CollectionUtils.isEmpty(newUserProfile.getElectronicSignatureAnswers())) {
            result.getElectronicSignatureAnswers().addAll(this.assembleCdxAnswers(newUserProfile.getElectronicSignatureAnswers()));
        }
        return result;
    }

    public List<NewUserProfile> assembleNewUserProfiles(List<RegistrationNewUserProfile> profiles) {
        List<NewUserProfile> results = new ArrayList<>();
        if (profiles != null) {
            for (RegistrationNewUserProfile p : profiles) {
                results.add(assembleNewUserProfile(p));
            }
        }
        return results;
    }

    public NewUserProfile assembleNewUserProfile(RegistrationNewUserProfile newUserProfile) {
        NewUserProfile result = new NewUserProfile();
        result.setUser(this.assembleUser(newUserProfile.getUser()));
        result.setOrganization(this.assembleOrg(newUserProfile.getOrganization()));
        if (newUserProfile.getRole() != null) { // can be null in case of retrieving users
            result.setRole(this.assembleRole(newUserProfile.getRole()));
        }
        if (!CollectionUtils.isEmpty(newUserProfile.getSecretAnswers())) {
            result.getSecretAnswers().addAll(this.assembleDomainAnswers(newUserProfile.getSecretAnswers()));
        }
        if (!CollectionUtils.isEmpty(newUserProfile.getElectronicSignatureAnswers())) {
            result.getElectronicSignatureAnswers().addAll(this.assembleDomainAnswers(newUserProfile.getElectronicSignatureAnswers()));
        }
        return result;
    }

    public IdentityProofingUserType assembleIdProofingUser(IdentityProofingProfile profile) throws Exception {
        IdentityProofingUserType result = new IdentityProofingUserType();
        BeanUtils.copyProperties(profile, result, "dateOfBirth");
        GregorianCalendar cal = GregorianCalendar.from(profile.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()));
        result.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
        return result;
    }

    public SignatureUserType assembleSignatureUser(User user, Organization organization) {
        SignatureUserType result = new SignatureUserType();
        result.setUserId(user.getUserId());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setMiddleInitial(user.getMiddleInitial());
        result.setOrganizationName(organization.getOrganizationName());
        return result;
    }

    public SignatureDocumentType assembleSignatureDocument(Document document) {
        SignatureDocumentType result = new SignatureDocumentType();
        result.setName(document.getName());
        result.setFormat(SignatureDocumentFormatType.BIN);
        result.setContent(getHandler(document.getContent()));
        return result;
    }

    public InboxAttachmentType assembleInboxAttachment(File file) {
        InboxAttachmentType result = new InboxAttachmentType();
        result.setContent(getHandler(file));
        result.setName(file.getName());
        return result;
    }

    public InboxAttachments assembleInboxAttachments(List<File> files) {
        InboxAttachments result = new InboxAttachments();
        for (File f : files) {
            result.getInboxAttachment().add(assembleInboxAttachment(f));
        }
        return result;
    }

    public DataHandler getHandler(File fileContent) {
        DocumentDataSource dds = new DocumentDataSource(fileContent, APPLICATION_OCTET_STREAM);
        return new DataHandler(dds);
    }
}
