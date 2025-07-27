package gift.controller;

import gift.dto.KakaoTokensResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.service.KakaoApiService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class KakaoApiController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final KakaoApiService kakaoApiService;

    @Value("${kakao.member.password}")
    private String kakaoMamberPassword;

    public KakaoApiController(MemberService memberService, JwtUtil jwtUtil, KakaoApiService kakaoApiService) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestParam String code){

        KakaoTokensResponseDto kakaoTokens = kakaoApiService.getKakaoTokens(code);
        KakaoUserInfoResponseDto kakaoUserInfo = kakaoApiService.getKakaoUserInfo(kakaoTokens.getAccessToken());
        String email = kakaoUserInfo.getId().toString()+ "@kakao.com";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email,kakaoMamberPassword);

        if(!memberService.existMember(memberRequestDto)) {
            memberService.saveMember(memberRequestDto);
        }
        String token = jwtUtil.generateToken(memberRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}
