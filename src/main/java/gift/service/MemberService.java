package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;

import java.util.Optional;

public interface MemberService {

    void saveMember(MemberRequestDto memberRequestDto);

    boolean existMember(MemberRequestDto memberRequestDto);

    Optional<Member> findByEmail(String email);
}
