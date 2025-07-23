package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberServiceImpl(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void saveMember(MemberRequestDto memberRequestDto) {
        memberRepository.save(memberRequestDto.toEntity());
    }

    @Override
    public boolean existMember(MemberRequestDto memberRequestDto) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(memberRequestDto.getEmail(), memberRequestDto.getPassword());

        return member.isPresent();
    }

    @Override
    public Optional<Member> findByEmail (String email) {
        return memberRepository.findByEmail(email);
    }
}
