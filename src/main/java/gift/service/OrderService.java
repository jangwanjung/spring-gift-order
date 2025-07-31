package gift.service;

import gift.LoginMember;
import gift.dto.OrderResponseDto;
import gift.dto.OrderResquestDto;
import gift.entity.Member;

public interface OrderService {

    OrderResponseDto createOrder(Member member, OrderResquestDto orderResquestDto);

    void deleteIfExistInWishList(Member member, Long optionId);

}
