package hyos1.myapp.controller;

import hyos1.myapp.dto.request.CouponCreateRequest;
import hyos1.myapp.dto.request.CouponUpdateRequest;
import hyos1.myapp.dto.response.CouponResponse;
import hyos1.myapp.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SecurityConfig에 ADMIN만 접근 가능하도록 설정함
 */
@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponCreateRequest request) {
        CouponResponse response = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 단일 쿠폰 조회
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> findById(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.findById(couponId));
    }

    // 전체 쿠폰 조회
    @GetMapping
    public ResponseEntity<List<CouponResponse>> findAll() {
        return ResponseEntity.ok(couponService.findAll());
    }

    // 쿠폰 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponUpdateRequest request) {
        CouponResponse response = couponService.updateCoupon(couponId, request);
        return ResponseEntity.ok(response);
    }
}