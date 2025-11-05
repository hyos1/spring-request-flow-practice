package hyos1.myapp.service;

import hyos1.myapp.dto.request.CouponCreateRequest;
import hyos1.myapp.dto.request.CouponUpdateRequest;
import hyos1.myapp.dto.response.CouponResponse;
import hyos1.myapp.entity.Coupon;
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

    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성
     */
    @Transactional
    public CouponResponse save(CouponCreateRequest request) {
        //시작일, 종료일 검증
        validationCouponDates(request.getStartDate(), request.getExpiredDate());

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

    /**
     * 전체 쿠폰 조회
     */
    public List<CouponResponse> findAll() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream().map(c -> CouponResponse.fromEntity(c))
                .collect(Collectors.toList());
    }

    /**
     * 단건 쿠폰 조회
     */
    public CouponResponse findById(Long couponId) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        return CouponResponse.fromEntity(coupon);
    }

    /**
     * 쿠폰 수정
     */
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CouponUpdateRequest request) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        coupon.updateCoupon(request.getAvailableCount(), request.getQuantity());
        return CouponResponse.fromEntity(coupon);
    }

    private void validationCouponDates(LocalDateTime startDate, LocalDateTime expiredDate) {
        if (expiredDate.isBefore(startDate)) {
            throw new IllegalArgumentException("쿠폰 종료일은 시작일 이후여야 합니다.");
        }
    }
}
