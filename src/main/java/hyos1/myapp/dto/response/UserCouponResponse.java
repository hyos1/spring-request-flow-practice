package hyos1.myapp.dto.response;

import hyos1.myapp.common.CouponStatus;
import hyos1.myapp.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserCouponResponse {

    private Long id;
    private String userName;
    private String couponName;
    private int availableCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public static UserCouponResponse fromEntity(UserCoupon userCoupon) {
        return UserCouponResponse.builder()
                .id(userCoupon.getId())
                .userName(userCoupon.getUser().getName())
                .couponName(userCoupon.getCoupon().getName())
                .availableCount(userCoupon.getAvailableCount())
                .createdAt(userCoupon.getCreatedAt())
                .expiredAt(userCoupon.getExpiredAt())
                .build();
    }
}
