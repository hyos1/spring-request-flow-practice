package hyos1.myapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateRequest {
    @NotNull(message = "1인당 이용가능 횟수를 입력해주세요.")
    private int availableCount;
    @NotNull(message = "수량을 입력해주세요.")
    private int quantity;
}
