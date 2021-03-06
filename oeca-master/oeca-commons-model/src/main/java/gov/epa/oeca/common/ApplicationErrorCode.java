package gov.epa.oeca.common;

/**
 * @author dfladung
 */
public enum ApplicationErrorCode {
    //Register
    E_UnknownUser, E_InvalidCredential, E_AccessDenied, E_InvalidToken, E_TokenExpired, E_AuthMethod, E_UserAlreadyExists,
    E_InsufficientPrivileges, E_WeakPassword, E_InvalidArgument, E_InvalidAnswerResetCode, E_MaxNumberOfResetAttemptsReached,
    E_AnswersAlreadyExist, E_RoleAlreadyExists, E_WrongUserId, E_ReachedMaxNumberOfAttempts, E_WrongAnswer,
    E_DuplicateAssociation, E_InvalidSignature, E_InternalError,
    //RegisterAuthErrorCode
    E_WrongIdPassword, E_AccountLocked, E_AccountExpired,
    // OECA
    E_Messaging, E_RemoteServiceError, E_Verification, E_Ineligible, E_Security, E_Validation,
    E_InvalidPermission, E_PermissionAlreadyExists, E_InvalidRequest, E_InvalidRequestStatus, E_RequestAlreadyExists
}
