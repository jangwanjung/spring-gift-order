package gift.service;

import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishList;
import gift.exception.WishListAccessDeniedException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishListServiceImpl(WishListRepository wishListRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Page<WishListResponseDto> getWishListByMemberId(Long memberId, Pageable pageable) {
        if(!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당유저를 찾을 수 없습니다. id = " + memberId);
        }
        Page<WishList> wishListPage = wishListRepository.findByMemberId(memberId, pageable);
        Page<WishListResponseDto> wishListResponseDtoPage = wishListPage.map(wishList -> new WishListResponseDto(
                wishList.getId(),
                wishList.getProduct().getId(),
                wishList.getQuantity(),
                wishList.getProduct().getName(),
                wishList.getProduct().getPrice(),
                wishList.getProduct().getImageUrl()
        ));

        return wishListResponseDtoPage;
    }

    @Override
    public void addWishList(Long memberId, WishListRequestDto wishListRequestDto) {
        Member member = findMemberById(memberId);
        Product product = findProductById(wishListRequestDto.getProductId());
        WishList wishList = new WishList(member,product,wishListRequestDto.getQuantity());
        wishListRepository.save(wishList);

    }

    @Override
    public void deleteWishList(Long memberId, Long wishListId) {
        validateWishListByMemberIdAndWishListId(memberId, wishListId);
        wishListRepository.deleteById(wishListId);
    }

    @Override
    public void validateWishListByMemberIdAndWishListId(Long memberId, Long wishListId){
        Member member = findMemberById(memberId);
        List<WishList> wishLists = member.getWishLists();
        for (WishList wishList : wishLists) {
            if (wishList.getId().equals(wishListId)) {
                return;
            }
        }
        throw new WishListAccessDeniedException();

    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "해당유저를 찾을 수 없습니다. id = " + memberId
                ));
    }


    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "해당상품을 찾을 수 없습니다. id = " + productId
                ));
    }
}
