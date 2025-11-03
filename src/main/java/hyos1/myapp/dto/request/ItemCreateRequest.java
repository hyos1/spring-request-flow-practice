package hyos1.myapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemCreateRequest {

    private String name;
    private int price;
    private int quantity;
}
