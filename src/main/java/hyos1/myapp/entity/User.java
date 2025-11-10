package hyos1.myapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hyos1.myapp.common.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@ToString(exclude = "orders")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean isDeleted;

    //회원이 주문 목록 조회 많이 할 것 같아서 양방향으로 결정
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    //회원이 가진 쿠폰 목록 조회가 많을 것 같아서 양방향으로 결정
    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCoupons = new ArrayList<>();

    private User(String name, String email, String password, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.isDeleted = false;
    }

    // ==생성 메서드==
    public static User createUser(String name, String email, String password, UserRole userRole) {
        return new User(name, email, password, userRole);
    }

    // ==연관관계 편의 메서드==
    public void addOrder(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
            order.setUser(this);
        }
    }

    public void addUserCoupon(UserCoupon userCoupon) {
        if (!userCoupons.contains(userCoupon)) {
            userCoupons.add(userCoupon);
            userCoupon.setUser(this);
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

}
