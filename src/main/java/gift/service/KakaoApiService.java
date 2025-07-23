package gift.service;

import gift.dto.KakaoTokensResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import org.springframework.http.ResponseEntity;

public interface KakaoApiService {


    KakaoTokensResponseDto getKakaoTokens(String code);

    KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken);
}
