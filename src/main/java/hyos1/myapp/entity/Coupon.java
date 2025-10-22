package hyos1.myapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;
    private String name;
    private int discountAmount;
    //생성할 수량
    private int quantity;
    //1인당 사용 가능 횟수
    private int availableCount;
    //쿠폰 사용 시작일 -> todo 스케쥴러 사용하기
    private LocalDateTime startDate;
    //쿠폰 만료일
    private LocalDateTime expiredDate;

    public Coupon(String name, int discountAmount, int quantity, int availableCount, LocalDateTime startDate, LocalDateTime expiredDate) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.quantity = quantity;
        this.availableCount = availableCount;
        this.startDate = startDate;
        this.expiredDate = expiredDate;
    }
}
