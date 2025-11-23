package hyos1.myapp.repository.coupon.datajpa;

import hyos1.myapp.entity.Coupon;

import java.util.Optional;

public interface CouponDataRepositoryCustom {
    Optional<Coupon> findByIdWithLock(Long couponId);
}
