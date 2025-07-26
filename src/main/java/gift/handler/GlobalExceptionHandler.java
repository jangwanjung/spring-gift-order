package gift.handler;

import gift.exception.AuthenticationException;
import gift.exception.KakaoApiException;
import gift.exception.WishListAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 첫 번째 오류 메시지만 가져오는 간단한 예시
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        String errorMessage = e.getMessage();
        return ResponseEntity.status(e.getStatusCode()).body(errorMessage);

    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(WishListAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleWishListAccessDeniedException(WishListAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(KakaoApiException.class)
    public ResponseEntity<String> handleKakaoApiException(KakaoApiException e) {
        return switch (e.getErrorType()) {
            case CLIENT_ERROR -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 카카오 로그인 요청입니다");
            case SERVER_ERROR -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("카카오 서버에 일시적 문제가 발생했습니다");
            case NETWORK_ERROR -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("카카오 서비스 연결에 실패했습니다");
            case PARSE_ERROR -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("카카오 응답 처리 중 오류가 발생했습니다");
        };
    }





}
