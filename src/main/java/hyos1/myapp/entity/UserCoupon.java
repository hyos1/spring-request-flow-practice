package hyos1.myapp.entity;

import hyos1.myapp.common.CouponStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupons")
@Getter @Setter
@NoArgsConstructor
public class UserCoupon extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private int availableCount;

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

    public static UserCoupon createUserCoupon(User user, Coupon coupon, int availableCount, CouponStatus couponStatus, LocalDateTime expiredAt) {
        UserCoupon userCoupon = new UserCoupon(availableCount, couponStatus, expiredAt);
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
        this.couponStatus = CouponStatus.USED;
    }
}
