package kakao.login.common;

public interface ResponseMessage {
    String SUCCESS = "SUCCESS";
    String VALIDATION_FAIL = "VALIDATION FAILED";
    String DUPLICATE_ID = "DUPLICATE ID";
    String SIGN_IN_FAIL = "Login information mismatch";
    String MAIL_FAIL = "Mail send failed.";
    String CERTIFICATION_FAIL = "CERTIFICATION FAILED";
    String DATABASE_ERROR = "DATABASE ERROR";
}
