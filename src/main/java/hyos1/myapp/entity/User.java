package hyos1.myapp.entity;

import hyos1.myapp.common.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCoupon;
    private List<Order> orders = new ArrayList<>();

    public User(String name, String email, String password, UserType userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.isDeleted = false;
    }

    //todo Setter 없애고 변경 메서드 추가
}
