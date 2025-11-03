package hyos1.myapp.repository.coupon.jpa;

import hyos1.myapp.entity.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);

    List<Coupon> findAll();
}
