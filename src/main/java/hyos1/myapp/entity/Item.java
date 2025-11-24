package hyos1.myapp.entity;

import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@Getter @Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Item extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    private Item(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Item createItem(String name, int price, int stock) {
        return new Item(name, price, stock);
    }

    public void changeName(String name) {
        this.name = name;
    }

    // ==비즈니스 로직==
    public void addQuantity(int quantity) {
        this.stock += quantity;
    }

    // 검증 + 재고 감소
    public void decreaseStock(int quantity) {
        if (this.stock - quantity < 0) {
            throw new ServerException(ErrorCode.ITEM_STOCK_EMPTY);
        }
        this.stock -= quantity;
    }

    //JdbcTemplate에서만 db에서 받은 ID값 할당을 위해 허용
    public void setId(Long id) {
        this.id = id;
    }

    public void updatePriceAndStock(int price, int stock) {
        this.price = price;
        this.stock = stock;
    }
}