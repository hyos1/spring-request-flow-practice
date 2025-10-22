package hyos1.myapp.entity;

import hyos1.myapp.common.CouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "user_coupon_id")
    private Long id;
    private CouponStatus couponStatus;
    private LocalDateTime expiredAt;
    private int availableCount;
    private User user;
    private Coupon coupon;

    public UserCoupon(LocalDateTime expiredAt, int availableCount, User user, Coupon coupon) {
        this.expiredAt = expiredAt;
        this.availableCount = availableCount;
        this.user = user;
        this.coupon = coupon;
    }
}
