package hyos1.myapp.entity;

import hyos1.myapp.common.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private boolean isDeleted;

    //회원이 주문 목록 조회 많이 할 것 같아서 양방향으로 결정
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    //회원이 가진 쿠폰 목록 조회가 많을 것 같아서 양방향으로 결정
    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCoupons = new ArrayList<>();

    private User(String name, String email, String password, UserType userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.isDeleted = false;
    }

    // ==생성 메서드==
    public static User createUser(String name, String email, String password, UserType userType) {
        return new User(name, email, password, userType);
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
}
