package hyos1.myapp.repository.usercoupon.jpa;

import hyos1.myapp.entity.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {

    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findById(Long userCouponId);
    List<UserCoupon> findAll();

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

}
