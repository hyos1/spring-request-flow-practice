package hyos1.myapp.dto.request;

import hyos1.myapp.common.OrderStatus;
import hyos1.myapp.entity.Order;
import hyos1.myapp.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private String userName;
    private String itemName;
    private int itemPrice;
    private int count;
    private String couponName;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;

    public static OrderResponse fromEntity(Order order) {
        OrderItem orderItem = order.getOrderItems().isEmpty() ? null : order.getOrderItems().get(0);
        return OrderResponse.builder()
                .orderId(order.getId())
                .userName(order.getUser().getName())
                .itemName(orderItem != null ? orderItem.getName() : null)
                .itemPrice(orderItem != null ? orderItem.getOrderPrice() : 0)
                .count(orderItem != null ? orderItem.getCount() : 0)
                .couponName(order.getUserCoupon() != null ? order.getUserCoupon().getCoupon().getName() : null)
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
