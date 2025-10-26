package hyos1.myapp.entity;

import hyos1.myapp.common.CouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "user_coupon_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;
    private LocalDateTime expiredAt;
    private int availableCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
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

    //직접 호출 x
    public void setUser(User user) {
        this.user = user;
    }

    private void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
