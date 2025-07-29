package gift.exception;

public class KakaoApiException extends RuntimeException {

    public enum ErrorType {
        CLIENT_ERROR,
        SERVER_ERROR,
        NETWORK_ERROR,
        PARSE_ERROR
    }

    private final ErrorType errorType;

    public KakaoApiException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public KakaoApiException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
