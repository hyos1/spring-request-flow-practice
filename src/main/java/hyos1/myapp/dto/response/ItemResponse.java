package hyos1.myapp.dto.response;

import hyos1.myapp.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private int price;
    private int quantity;

    public static ItemResponse fromEntity(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getPrice(), item.getQuantity());
    }
}
