package hyos1.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class Item extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int quantity;

    private Item(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static Item createItem(String name, int price, int quantity) {
        return new Item(name, price, quantity);
    }

    public void changeName(String name) {
        this.name = name;
    }

    // ==비즈니스 로직==
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        int restStock = this.quantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }

    //JdbcTemplate에서만 db에서 받은 ID값 할당을 위해 허용
    public void setId(Long id) {
        this.id = id;
    }
}
