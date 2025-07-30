package gift.controller;

import gift.LoginMember;
import gift.dto.KakaoTokensResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
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
    public ResponseEntity<String> login(@LoginMember Member member, @RequestParam String code){

        KakaoTokensResponseDto kakaoTokens = kakaoApiService.getKakaoTokens(code);

        memberService.changeKakaoAccessToken(member,kakaoTokens.getAccessToken());
        return ResponseEntity.ok("카카오 엑세스 토큰이 등록되었습니다.");
    }
}
