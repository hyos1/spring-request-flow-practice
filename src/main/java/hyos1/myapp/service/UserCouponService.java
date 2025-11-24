package hyos1.myapp.service;

import hyos1.myapp.common.CouponStatus;
import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.dto.response.UserCouponResponse;
import hyos1.myapp.entity.Coupon;
import hyos1.myapp.entity.User;
import hyos1.myapp.entity.UserCoupon;
import hyos1.myapp.repository.coupon.datajpa.CouponDataRepository;
import hyos1.myapp.repository.user.datajpa.UserDataRepository;
import hyos1.myapp.repository.usercoupon.datajpa.UserCouponDataRepository;
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

    // 순수 jpa
//    private final UserCouponJpaRepository userCouponRepository;
//    private final UserRepository userRepository;
//    private final CouponRepository couponRepository;
    // data jpa
    private final UserCouponDataRepository userCouponRepository;
    private final UserDataRepository userRepository;
    private final CouponDataRepository couponRepository;


    /**
     * [사용자]쿠폰 발급
     */
    @Transactional
    public UserCouponResponse issueCoupon(Long userId, Long couponId) {

        //락 없이 유저 먼저 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND));

        //쿠폰 조회 + PESSIMISTIC_WRITE 락
        Coupon coupon = couponRepository.findByIdWithLock(couponId).orElseThrow(
                () -> new ClientException(ErrorCode.COUPON_NOT_FOUND));

        //중복 발급 검증
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new ClientException(ErrorCode.COUPON_ALREADY_ISSUED);
        }

        //발급 가능 기간 검증
        if (!coupon.isAvailableNow(LocalDateTime.now())) {
            throw new ClientException(ErrorCode.COUPON_NOT_ISSUABLE_NOW);
        }

        //발급 가능한 수량이 있는지 검증
        if (!coupon.canIssue()) {
            throw new ServerException(ErrorCode.COUPON_QUANTITY_EMPTY);
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
     * 사용자의 쿠폰 단건 조회
     */
    public UserCouponResponse findByUserIdAndCouponId(Long userId, Long couponId) {
        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_COUPON_NOT_FOUND)
        );
        return UserCouponResponse.fromEntity(userCoupon);
    }

    /**
     * 사용자의 쿠폰 전체 조회
     */
    public List<UserCouponResponse> findAllByUserId(Long userId) {
        List<UserCoupon> result = userCouponRepository.findAllByUserId(userId);
        return result.stream()
                .map(uc -> UserCouponResponse.fromEntity(uc))
                .collect(Collectors.toList());
    }

    /**
     * [관리자]UserCoupon 단건 조회
     */
    public UserCouponResponse findById(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_COUPON_NOT_FOUND_ADMIN));
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