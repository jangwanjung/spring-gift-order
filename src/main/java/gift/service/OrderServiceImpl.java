package gift.service;

import gift.LoginMember;
import gift.dto.OrderResponseDto;
import gift.dto.OrderResquestDto;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.WishList;
import gift.repository.OptionRepository;
import gift.repository.OrderRespository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final OrderRespository orderRespository;
    private final KakaoApiService kakaoApiService;
    private final WishListService wishListService;

    public OrderServiceImpl(OptionService optionService, OptionRepository optionRepository,
                            OrderRespository orderRespository, KakaoApiService kakaoApiService,
                            WishListService wishListService) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.orderRespository = orderRespository;
        this.kakaoApiService = kakaoApiService;
        this.wishListService = wishListService;
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(@LoginMember Member member, OrderResquestDto orderResquestDto) {
        Option option = optionService.findOptionById(orderResquestDto.getOptionId());
        optionService.sellOption(orderResquestDto.getOptionId(), orderResquestDto.getQuantity());
        deleteIfExistInWishList(member,orderResquestDto.getOptionId());
        Order saveOrder = orderRespository.save(new Order(member,option,orderResquestDto.getQuantity(), orderResquestDto.getMessage()));
        kakaoApiService.messageToMe(member.getKakaoAccessToken(), orderResquestDto.getMessage());
        return new OrderResponseDto(saveOrder.getId(), saveOrder.getOption().getId(),saveOrder.getQuantity(),saveOrder.getOrderDateTime(),saveOrder.getMessage());

    }

    @Override
    public void deleteIfExistInWishList(Member member, Long optionId) {
        List<WishList> wishLists = member.getWishLists();

        wishLists.stream()
                .filter(wishList -> wishList.getProduct().getOptions().stream()
                        .anyMatch(option -> option.getId().equals(optionId)))
                .findFirst()
                .ifPresent(wishList ->  wishListService.deleteWishList(member.getId(), wishList.getId()));
    }
}
