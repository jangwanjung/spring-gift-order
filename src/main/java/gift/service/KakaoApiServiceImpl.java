package gift.service;

import gift.dto.KakaoTokenRequestDto;
import gift.dto.KakaoTokensResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import gift.exception.KakaoApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
public class KakaoApiServiceImpl implements KakaoApiService {

    private final static String KAKAO_API_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";


    private final RestClient restClient = RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory() {{
                setConnectTimeout(5000);  // 5초
                setReadTimeout(10000);    // 10초
            }})
            .build();


    @Autowired
    private KakaoTokenRequestDto kakaoTokenRequestDto;


    @Override
    public KakaoTokensResponseDto getKakaoTokens(String code) {
        MultiValueMap<String,String> body = kakaoTokenRequestDto.makeBody(code);

        try {
            return restClient.post()
                    .uri(KAKAO_API_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new KakaoApiException(KakaoApiException.ErrorType.CLIENT_ERROR, "카카오 API 클라이언트 오류: " + response.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new KakaoApiException(KakaoApiException.ErrorType.SERVER_ERROR, "카카오 서버 오류: " + response.getStatusCode());
                    })
                    .body(KakaoTokensResponseDto.class);

        } catch (ResourceAccessException e) {
            throw new KakaoApiException(KakaoApiException.ErrorType.NETWORK_ERROR, "카카오 API 연결 실패 (타임아웃 또는 네트워크 오류)", e);
        } catch (HttpMessageNotReadableException e) {
            throw new KakaoApiException(KakaoApiException.ErrorType.PARSE_ERROR, "카카오 API 응답 파싱 실패", e);
        }
    }

    @Override
    public KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken) {

        try {
            return restClient.post()
                    .uri(KAKAO_USER_INFO_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new KakaoApiException(KakaoApiException.ErrorType.CLIENT_ERROR, "카카오 사용자 정보 조회 클라이언트 오류: " + response.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new KakaoApiException(KakaoApiException.ErrorType.SERVER_ERROR, "카카오 사용자 정보 조회 서버 오류: " + response.getStatusCode());
                    })
                    .body(KakaoUserInfoResponseDto.class);

        } catch (ResourceAccessException e) {
            throw new KakaoApiException(KakaoApiException.ErrorType.NETWORK_ERROR, "카카오 사용자 정보 API 연결 실패 (타임아웃 또는 네트워크 오류)", e);
        } catch (HttpMessageNotReadableException e) {
            throw new KakaoApiException(KakaoApiException.ErrorType.PARSE_ERROR, "카카오 사용자 정보 API 응답 파싱 실패", e);
        }
    }
}
