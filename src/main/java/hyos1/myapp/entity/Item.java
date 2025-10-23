package hyos1.myapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private OrderItem orderItem;

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void sellItem(int quantity) {
        if (stockQuantity - quantity < 0) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        stockQuantity -= quantity;
    }

    public void cancel(int quantity) {
        stockQuantity += quantity;
    }
}
