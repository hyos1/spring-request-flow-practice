package hyos1.myapp.entity;

import hyos1.myapp.common.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hyos1.myapp.common.OrderStatus.CANCEL;
import static hyos1.myapp.common.OrderStatus.ORDER;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private int finalPrice; // 최종 금액

    @Column(nullable = false)
    @ToString.Include
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @ToString.Include
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id", nullable = true)
    private UserCoupon userCoupon;

    //주문의 상세정보는 자주 필요하므로 양방향으로 결정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // ===== 주문 생성 =====
    public static Order createOrder(User user, List<OrderItem> orderItems, UserCoupon userCoupon) {
        Order order = new Order();
        order.orderStatus = ORDER; //주문시 ORDER 세팅
        user.addOrder(order); //양방향 편의 메서드 사용

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        // 쿠폰 사용시에만 연관관계 설정
        if (userCoupon != null) {
            order.setUserCoupon(userCoupon);
        }

        order.calculateFinalPrice();
        return order;
    }

    // ==연관관계 편의 메서드==
    public void setUser(User user) {
        this.user = user;
    }

    public void setUserCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // ==비즈니스 로직==
    public void cancelOrder() {
        // 주문 상태 변경
        this.setOrderStatus(CANCEL);

        // 주문 상품 재고 복원
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }

        // 쿠폰 복원
//        if (userCoupon != null) {
//            userCoupon.restore();
//        }
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    private void calculateFinalPrice() {
        int totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        //쿠폰 적용
        if (userCoupon != null) {
            totalPrice -= userCoupon.getCoupon().getDiscountAmount();
        }

        // 음수 방지
        if (totalPrice < 0) {
            totalPrice = 0;
        }

        this.finalPrice = totalPrice;
    }
}