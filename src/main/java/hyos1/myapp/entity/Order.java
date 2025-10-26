package hyos1.myapp.entity;

import hyos1.myapp.common.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    //주문의 상세정보는 자주 필요하므로 양방향으로 결정
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    //쿠폰 사용없이 주문 생성
    public static Order createOrder(User user, OrderItem... orderItems) {
        Order order = new Order();
        order.orderStatus = OrderStatus.ORDER; //주문시 ORDER 세팅
        user.addOrder(order); //양방향 편의 메서드 사용
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }
    //쿠폰 사용하는 주문 생성
    public static Order createOrderWithCoupon(User user, UserCoupon userCoupon, OrderItem... orderItems) {
        Order order = new Order();
        order.orderStatus = OrderStatus.ORDER;
        user.addOrder(order); //양방향 편의 메서드 사용
        order.setUserCoupon(userCoupon); //단방향 메서드
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
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
}