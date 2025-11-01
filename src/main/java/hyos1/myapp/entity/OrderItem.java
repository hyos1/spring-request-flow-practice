package hyos1.myapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "order") //순환참조 문제생김
@EqualsAndHashCode
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    private String name;
    private int orderPrice;
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private OrderItem(String name, int orderPrice, int count) {
        this.name = name;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, String name, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem(name, orderPrice, count);
        orderItem.setItem(item);

        item.removeQuantity(count);
        return orderItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // ==비즈니스 로직==
    public void cancel() {
        getItem().addQuantity(count);
    }

    // ==조회 로직==
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}