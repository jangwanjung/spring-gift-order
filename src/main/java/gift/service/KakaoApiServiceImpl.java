package gift.service;

import gift.dto.KakaoTokenRequestDto;
import gift.dto.KakaoTokensResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

@Service
public class KakaoApiServiceImpl implements KakaoApiService {

    private final static String KAKAO_API_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";


    private final RestClient restClient = RestClient.builder().build();

    @Autowired
    private KakaoTokenRequestDto kakaoTokenRequestDto;


    @Override
    public KakaoTokensResponseDto getKakaoTokens(String code) {
        MultiValueMap<String,String> body = kakaoTokenRequestDto.makeBody(code);


        return restClient.post()
                .uri(KAKAO_API_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(KakaoTokensResponseDto.class);
    }

    @Override
    public KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken) {

        return restClient.post()
                .uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(KakaoUserInfoResponseDto.class);
    }
}
