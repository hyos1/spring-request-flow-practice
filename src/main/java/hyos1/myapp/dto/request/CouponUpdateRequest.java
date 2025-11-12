package hyos1.myapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateRequest {
    @Positive(message = "1인당 이용가능 횟수는 0 이상이어야 합니다.")
    private int availableCount;
    @Positive(message = "수량은 0 이상이어야 합니다.")
    private int quantity;
}