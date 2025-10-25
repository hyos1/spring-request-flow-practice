package hyos1.myapp.entity;

import hyos1.myapp.common.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
    private OrderItem orderItem;

    public Order(OrderStatus orderStatus, LocalDateTime createdAt) {
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    //todo 총 주문 가격 합은 OrderItem for 문으로 메서드 만들기
}