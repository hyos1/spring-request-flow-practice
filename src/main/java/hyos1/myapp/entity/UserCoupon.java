package hyos1.myapp.entity;

import hyos1.myapp.enums.CouponStatus;
import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupons",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "coupon_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class UserCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private int availableCount; // 이용 가능 횟수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    private UserCoupon(int availableCount, CouponStatus couponStatus, LocalDateTime expiredAt) {
        this.availableCount = availableCount;
        this.couponStatus = couponStatus;
        this.expiredAt = expiredAt;
    }

    public static UserCoupon createUserCoupon(User user, Coupon coupon) {
        UserCoupon userCoupon = new UserCoupon(coupon.getAvailableCount(), CouponStatus.UNUSED, coupon.getExpiredDate());
        user.addUserCoupon(userCoupon);
        userCoupon.setCoupon(coupon);
        return userCoupon;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void use() {
        this.availableCount -= 1;
        if (availableCount == 0) {
            this.couponStatus = CouponStatus.USED;
        }
    }

    // 사용 가능 횟수 검증
    public void checkAvailable() {
        if (this.availableCount <= 0) {
            throw new ClientException(ErrorCode.COUPON_USAGE_EXHAUSTED);
        }
    }

    // 쿠폰 만료됐는지 확인
    public void checkNotExpired(LocalDateTime now) {
        if (this.expiredAt.isBefore(now)) {
            this.couponStatus = CouponStatus.EXPIRED;
            throw new ClientException(ErrorCode.COUPON_EXPIRED);
        }
    }

    // 본인 쿠폰인지 확인
    public void checkOwner(Long userId) {
        if (!userId.equals(this.getUser().getId())) {
            throw new ClientException(ErrorCode.COUPON_NOT_OWNER);
        }
    }
}