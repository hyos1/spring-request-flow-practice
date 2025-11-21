package hyos1.myapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
    private Long couponId; // null가능

    @Getter
    @NoArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        private Long itemId;
        @Min(1)
        private int quantity;
    }
}
