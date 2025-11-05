package hyos1.myapp.service;

import hyos1.myapp.common.CouponStatus;
import hyos1.myapp.dto.response.UserCouponResponse;
import hyos1.myapp.entity.Coupon;
import hyos1.myapp.entity.User;
import hyos1.myapp.entity.UserCoupon;
import hyos1.myapp.repository.coupon.jpa.CouponRepository;
import hyos1.myapp.repository.user.jpa.UserRepository;
import hyos1.myapp.repository.usercoupon.jpa.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    /**
     * [사용자]쿠폰 발급
     */
    @Transactional
    public UserCouponResponse issueCoupon(Long userId, Long couponId) {

        //락 없이 유저 먼저 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //쿠폰 조회 + PESSIMISTIC_WRITE 락
        Coupon coupon = couponRepository.findByIdWithLock(couponId).orElseThrow(
                () -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        //검증 로직
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalStateException("이미 발급된 쿠폰입니다.");
        }

        if (!coupon.isAvailable(LocalDateTime.now())) {
            throw new IllegalStateException("발급 가능한 날짜가 아닙니다.");
        }
        if (!coupon.canIssue()) {
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }

        //수량 감소
        coupon.decreaseQuantity();

        //사용자 쿠폰 생성
        UserCoupon userCoupon = UserCoupon.createUserCoupon(
                user,
                coupon,
                coupon.getAvailableCount(),
                CouponStatus.UNUSED,
                coupon.getExpiredDate()
        );
        userCouponRepository.save(userCoupon);

        return UserCouponResponse.fromEntity(userCoupon);
    }

    /**
     * 사용자 쿠폰 단건 조회
     */
    public UserCouponResponse findById(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new IllegalArgumentException("사용자 쿠폰을 찾을 수 없습니다."));
        return UserCouponResponse.fromEntity(userCoupon);
    }

    /**
     * [관리자]전체 사용자 쿠폰 목록 조회
     */
    public List<UserCouponResponse> findAll() {
        List<UserCoupon> result = userCouponRepository.findAll();
        return result.stream().map(uc -> UserCouponResponse.fromEntity(uc))
                .collect(Collectors.toList());
    }
}
