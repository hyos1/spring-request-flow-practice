package hyos1.myapp.repository.item;

import lombok.Data;

@Data
public class ItemSearchCond {

    private String name;
    private Integer maxPrice;

    public ItemSearchCond() {
    }

    public ItemSearchCond(String name, Integer maxPrice) {
        this.name = name;
        this.maxPrice = maxPrice;
    }
}
