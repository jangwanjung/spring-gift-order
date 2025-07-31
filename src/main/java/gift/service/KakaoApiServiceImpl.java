package gift.service;

import gift.config.KakaoProperties;
import gift.dto.*;
import gift.exception.KakaoApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
public class KakaoApiServiceImpl implements KakaoApiService {

    private final RestClient restClient;

    @Autowired
    private KakaoTokenRequestDto kakaoTokenRequestDto;
    @Autowired
    private KakaoProperties kakaoProperties;

    public KakaoApiServiceImpl(KakaoProperties kakaoProperties) {
        this.restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory() {{
                    setConnectTimeout(kakaoProperties.getConnectTimeout());
                    setReadTimeout(kakaoProperties.getReadTimeout());
                }})
                .build();
    }


    @Override
    @Retryable(
            value = {KakaoApiException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public KakaoTokensResponseDto getKakaoTokens(String code) {
        MultiValueMap<String,String> body = kakaoTokenRequestDto.makeBody(code);

        try {
            return restClient.post()
                    .uri(kakaoProperties.getApiUrl())
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
    @Retryable(
            value = {KakaoApiException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken) {

        try {
            return restClient.post()
                    .uri(kakaoProperties.getUserInfoUrl())
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

    @Override
    public void messageToMe(String accessToken, String text) {
        try {
            TemplateObject templateObject = new TemplateObject("text", text, "url", "바로 확인");


            restClient.post()
                    .uri(kakaoProperties.getMessageUrl())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(templateObject.makeBody())
                    .retrieve()
                    .toEntity(resultCode.class);

        } catch (HttpClientErrorException e){
            throw new KakaoApiException(KakaoApiException.ErrorType.ACCESS_TOKEN_ERROR,"엑세스 토큰이 잘못되었습니다.",e);
        }



    }
}
