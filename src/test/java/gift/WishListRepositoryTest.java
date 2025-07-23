package gift;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishList;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

@DataJpaTest
public class WishListRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Test
    void 위시리스트_생성_테스트(){
        Member member = memberRepository.save(new Member("tjdrj530@gmail.com", "12345", "USER"));
        Product product = productRepository.save(new Product("장난감",10000,"robot.jpg"));
        Integer quantity = 3;

        WishList saveWishList = wishListRepository.save(new WishList(member, product, quantity));

        Optional<WishList> wishList = wishListRepository.findById(saveWishList.getId());
        assertThat(wishList.isPresent()).isTrue();
        assertThat(wishList.get().getQuantity()).isEqualTo(quantity);
        assertThat(wishList.get().getProduct()).isEqualTo(product);
        assertThat(wishList.get().getMember()).isEqualTo(member);
    }
}
