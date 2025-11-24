package hyos1.myapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    @NotNull
    @Positive
    private int price;
    @NotNull
    @Positive
    private int stock;
}
