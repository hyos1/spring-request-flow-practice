package hyos1.myapp.service;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.dto.request.CouponCreateRequest;
import hyos1.myapp.dto.request.CouponUpdateRequest;
import hyos1.myapp.dto.response.CouponResponse;
import hyos1.myapp.entity.Coupon;
import hyos1.myapp.repository.coupon.datajpa.CouponDataRepository;
import hyos1.myapp.repository.coupon.jpa.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

//    private final CouponRepository couponRepository; // 순수 jpa
    private final CouponDataRepository couponRepository; // data jpa

    // [관리자] 쿠폰 생성
    @Transactional
    public CouponResponse createCoupon(CouponCreateRequest request) {
        if (couponRepository.existsByName(request.getName())) {
            throw new ClientException(ErrorCode.COUPON_ALREADY_EXISTS);
        }

        Coupon coupon = Coupon.createCoupon(
                request.getName(),
                request.getDiscountAmount(),
                request.getQuantity(),
                request.getAvailableCount(),
                request.getStartDate(),
                request.getExpiredDate()
        );
        Coupon savedCoupon = couponRepository.save(coupon);

        return CouponResponse.fromEntity(savedCoupon);
    }

    // 쿠폰 단건 조회
    public CouponResponse findById(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ClientException(ErrorCode.COUPON_NOT_FOUND));

        return CouponResponse.fromEntity(coupon);
    }

    // 쿠폰 전체 조회
    public List<CouponResponse> findAll() {
        List<Coupon> coupons = couponRepository.findAll();

        return coupons.stream().map(c -> CouponResponse.fromEntity(c))
                .collect(Collectors.toList());
    }

    // [관리자] 쿠폰 수정
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CouponUpdateRequest request) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new ClientException(ErrorCode.COUPON_NOT_FOUND));

        coupon.updateCoupon(request.getAvailableCount(), request.getQuantity());

        return CouponResponse.fromEntity(coupon);
    }
}