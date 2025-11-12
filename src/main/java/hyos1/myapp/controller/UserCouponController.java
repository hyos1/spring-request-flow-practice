package hyos1.myapp.controller;

import hyos1.myapp.dto.response.UserCouponResponse;
import hyos1.myapp.entity.AuthUser;
import hyos1.myapp.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    // [사용자] 쿠폰 발급 요청
    @PostMapping("/{couponId}")
    public ResponseEntity<UserCouponResponse> issueCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal AuthUser authUser) {
        UserCouponResponse response = userCouponService.issueCoupon(authUser.getUserId(), couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // [사용자] 본인 쿠폰 단건 조회
    @GetMapping("/{couponId}")
    public ResponseEntity<UserCouponResponse> findByUserIdAndCouponId(
            @PathVariable Long couponId,
            @AuthenticationPrincipal AuthUser authUser) {
        UserCouponResponse result = userCouponService.findByUserIdAndCouponId(authUser.getUserId(), couponId);
        return ResponseEntity.ok(result);
    }

    // [사용자] 본인 쿠폰 전체 조회
    @GetMapping
    public ResponseEntity<List<UserCouponResponse>> findAllByUser(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(userCouponService.findAllByUserId(authUser.getUserId()));
    }

    // [관리자] 사용자 쿠폰 단건 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{userCouponId}")
    public ResponseEntity<UserCouponResponse> findById(@PathVariable Long userCouponId) {
        UserCouponResponse response = userCouponService.findById(userCouponId);
        return ResponseEntity.ok(response);
    }

    // [관리자] 전체 사용자 쿠폰 목록 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<UserCouponResponse>> findAll() {
        List<UserCouponResponse> results = userCouponService.findAll();
        return ResponseEntity.ok(results);
    }
}