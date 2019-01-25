package gov.epa.oeca.services.registration.application;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.dto.datatable.DataTableConfig;
import gov.epa.oeca.common.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.common.domain.registration.*;
import net.exchangenetwork.wsdl.register.streamlined._1.*;
import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author dfladung
 */
public interface RegistrationService {

    String ESA_APPROVER = "OECA-APPLICATION";
    String ESA_DATAFLOW = "CDX";

    List<Dataflow> retrieveDataflowsByCriteria(Map<String, String> criteria) throws ApplicationException;

    List<Role> retrieveRolesByCriteria(Map<String, String> criteria) throws ApplicationException;

    List<RegistrationUserTitle> retrieveTitles() throws ApplicationException;

    List<RegistrationUserSuffix> retrieveSuffixes() throws ApplicationException;

    List<RegistrationQuestion> retrieveAllSecretQuestions() throws ApplicationException;

    List<RegistrationQuestion> retrieveAllElectronicSignatureQuestions() throws ApplicationException;

    List<RegistrationState> retrieveAllStates() throws ApplicationException;

    List<RegistrationCountry> retrieveAllCountries() throws ApplicationException;

    List<Organization> searchOrganizations(Map<String, Object> criteria) throws ApplicationException;

    Boolean isUserIdAvailable(User user) throws ApplicationException;

    Boolean validatePassword(User user) throws ApplicationException;

    NewUserProfile createUser(NewUserProfile profile) throws ApplicationException;

    Boolean validateIdentity(IdentityProofingProfile profile) throws ApplicationException;

    File generateEsa(NewUserProfile profile) throws ApplicationException;

    Boolean signEsa(NewUserProfile profile) throws ApplicationException;

    RegistrationUserSearchResult searchUsers(Map<String, Object> criteria) throws ApplicationException;

    RegistrationUserSearchResult searchUsers(DataTableCriteria<Map<String, Object>> criteria) throws ApplicationException;

    String authenticateUser(String userId, String password) throws ApplicationException;

    void changePassword(String userId, String newPassword, Boolean expire) throws ApplicationException;

    List<Role> retrieveUserRolesByCriteria(RegistrationRoleSearchCriteria criteria) throws ApplicationException;

    List<Organization> retrieveOrganizationsByUserRoleAndSubject(String userId, Long roleId,
                                                                 String subject) throws ApplicationException;

}
