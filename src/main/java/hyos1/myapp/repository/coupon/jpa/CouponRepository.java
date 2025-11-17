package hyos1.myapp.repository.coupon.jpa;

import hyos1.myapp.entity.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);

    // 비관적 락으로 쿠폰 조회
    Optional<Coupon> findByIdWithLock(Long couponId);

    List<Coupon> findAll();

    // 동일 이름 존재 확인
    boolean existsByName(String name);
}