package hyos1.myapp.dto.response;

import hyos1.myapp.common.OrderStatus;
import hyos1.myapp.entity.Order;
import hyos1.myapp.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private String userName;
    private String couponName;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt; // 주문 시간
    private int finalPrice;
    private List<OrderItemResponse> orderItems;

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userName(order.getUser().getName())
                .couponName(order.getUserCoupon() != null ? order.getUserCoupon().getCoupon().getName() : null)
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .finalPrice(order.getFinalPrice())
                .orderItems(
                        order.getOrderItems().stream()
                                .map(oi -> new OrderItemResponse(oi))
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Getter
    static class OrderItemResponse {
        private String itemName;
        private int itemPrice;
        private int quantity;

        public OrderItemResponse(OrderItem orderItem) {
            itemName = orderItem.getName();
            itemPrice = orderItem.getOrderPrice();
            quantity = orderItem.getQuantity();
        }
    }
}
