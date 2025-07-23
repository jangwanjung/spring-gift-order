package gift.controller;

import gift.dto.KakaoTokensResponseDto;
import gift.dto.KakaoUserInfoResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.service.KakaoApiService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.sql.SQLOutput;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final KakaoApiService kakaoApiService;

    @Value("${kakao.member.password}")
    private String kakaoMamberPassword;

    public MemberController(MemberService memberService, JwtUtil jwtUtil, KakaoApiService kakaoApiService) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        String token = jwtUtil.generateToken(memberRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {

      if(!memberService.existMember(memberRequestDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        String token = jwtUtil.generateToken(memberRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
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
