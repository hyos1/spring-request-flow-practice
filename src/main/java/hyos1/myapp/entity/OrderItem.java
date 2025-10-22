package hyos1.myapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;
    private String name;
    private int orderPrice;
    private int quantity;
    private Item item;
    private Order order;

    public OrderItem(String name, int orderPrice, int quantity) {
        this.name = name;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }
}