package hyos1.myapp.dto.response;

import hyos1.myapp.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {

    private Long id;
    private int availableCount;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime expiredDate;

    public static CouponResponse fromEntity(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .availableCount(coupon.getAvailableCount())
                .quantity(coupon.getQuantity())
                .createdAt(coupon.getCreatedAt())
                .startDate(coupon.getStartDate())
                .expiredDate(coupon.getExpiredDate())
                .build();
    }
}
