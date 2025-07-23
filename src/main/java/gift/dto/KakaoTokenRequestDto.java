package gift.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

@Component
public class KakaoTokenRequestDto {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public MultiValueMap<String, String> makeBody(String code){
        MultiValueMap <String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", "http://localhost:8080/api/members/login");
        body.add("client_id", kakaoApiKey);
        return body;
    }
}
