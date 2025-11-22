package hyos1.myapp.repository.usercoupon.datajpa;

import hyos1.myapp.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponDataRepository extends JpaRepository<UserCoupon, Long> {
    // 사용자가 발급받은 쿠폰 단건 조회
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    // 사용자가 발급받은 쿠폰 전체 조회
    List<UserCoupon> findAllByUserId(Long userId);

    // 사용자가 해당 쿠폰 발급 받았었는지 확인
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

}
