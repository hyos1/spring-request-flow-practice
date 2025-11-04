package hyos1.myapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//관리자용 쿠폰 생성 혹은 수정 요청용 DTO
@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    private String name;

    @Positive(message = "할인 금액은 0보다 커야 합니다.")
    private int discountAmount;
    @Positive(message = "생성 수량은 0보다 커야합니다.")
    private int quantity;
    @Positive(message = "사용 가능 횟수는 1회 이상이어야 합니다.")
    private int availableCount;
    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate; // 쿠폰 사용가능 시작일, "생성일X"
    @NotNull
    @Future
    private LocalDateTime expiredDate;// 쿠폰 만료일

    public CouponCreateRequest(String name, int discountAmount, int quantity, int availableCount, LocalDateTime startDate, LocalDateTime expiredDate) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.quantity = quantity;
        this.availableCount = availableCount;
        this.startDate = startDate;
        this.expiredDate = expiredDate;
    }
}
