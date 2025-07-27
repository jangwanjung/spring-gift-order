package gift.service;

import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListService {

    Page<WishListResponseDto> getWishListByMemberId(Long memberId, Pageable pageable);

    void addWishList(Long memberId, WishListRequestDto wishListRequestDto);

    void deleteWishList(Long memberId, Long wishListId);

    void validateWishListByMemberIdAndWishListId(Long memberId, Long wishListId);
}
