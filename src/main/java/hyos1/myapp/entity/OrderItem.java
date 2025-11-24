package hyos1.myapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private String name;

    @Column(nullable = false)
    @ToString.Include
    private int orderPrice; // 주문 당시 상품 1개의 가격

    @Column(nullable = false)
    @ToString.Include
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private OrderItem(String name, int orderPrice, int quantity) {
        this.name = name;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    public static OrderItem createOrderItem(Item item, int quantity) {
        OrderItem orderItem = new OrderItem(item.getName(), item.getPrice(), quantity);
        orderItem.setItem(item);

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
        getItem().addQuantity(quantity);
    }

    // ==조회 로직==
    public int getTotalPrice() {
        return getOrderPrice() * getQuantity();
    }
}