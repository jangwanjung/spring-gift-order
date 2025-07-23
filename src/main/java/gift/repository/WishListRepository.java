package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Page<WishList> findByMemberId(Long memberId, Pageable pageable);


}
