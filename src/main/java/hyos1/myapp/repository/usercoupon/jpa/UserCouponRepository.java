package hyos1.myapp.repository.usercoupon.jpa;

import hyos1.myapp.entity.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {

    // 유저가 쿠폰 발급
    UserCoupon save(UserCoupon userCoupon);

    // 유저가 발급받은 쿠폰 단건 조회
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    // 유저가 발급받은 쿠폰 전체 조회
    List<UserCoupon> findAllByUserId(Long userId);

    // [관리자]UserCoupon 단건 조회
    Optional<UserCoupon> findById(Long userCouponId);

    // [관리자]모든 회원이 발급받은 쿠폰 전체 조회
    List<UserCoupon> findAll();

    //발급받은 적 있는지 확인
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
