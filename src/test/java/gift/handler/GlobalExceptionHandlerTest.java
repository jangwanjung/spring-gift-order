package gift.handler;

import gift.exception.KakaoApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void 클라이언트_오류_예외_처리() {
        // given
        KakaoApiException exception = new KakaoApiException(
                KakaoApiException.ErrorType.CLIENT_ERROR, 
                "카카오 API 클라이언트 오류"
        );
        
        // when
        ResponseEntity<String> response = globalExceptionHandler.handleKakaoApiException(exception);
        
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("잘못된 카카오 로그인 요청입니다");
    }

    @Test
    void 서버_오류_예외_처리() {
        // given
        KakaoApiException exception = new KakaoApiException(
                KakaoApiException.ErrorType.SERVER_ERROR, 
                "카카오 서버 오류"
        );
        
        // when
        ResponseEntity<String> response = globalExceptionHandler.handleKakaoApiException(exception);
        
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isEqualTo("카카오 서버에 일시적 문제가 발생했습니다");
    }

    @Test
    void 네트워크_오류_예외_처리() {
        // given
        KakaoApiException exception = new KakaoApiException(
                KakaoApiException.ErrorType.NETWORK_ERROR, 
                "네트워크 연결 실패"
        );
        
        // when
        ResponseEntity<String> response = globalExceptionHandler.handleKakaoApiException(exception);
        
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isEqualTo("카카오 서비스 연결에 실패했습니다");
    }

    @Test
    void 파싱_오류_예외_처리() {
        // given
        KakaoApiException exception = new KakaoApiException(
                KakaoApiException.ErrorType.PARSE_ERROR, 
                "JSON 파싱 실패"
        );
        
        // when
        ResponseEntity<String> response = globalExceptionHandler.handleKakaoApiException(exception);
        
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isEqualTo("카카오 응답 처리 중 오류가 발생했습니다");
    }
}
