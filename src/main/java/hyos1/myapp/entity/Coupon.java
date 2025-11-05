package hyos1.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private int discountAmount;
    //생성할 수량
    @Column(nullable = false)
    private int quantity;
    //1인당 사용 가능 횟수
    @Column(nullable = false)
    private int availableCount;
    //쿠폰 사용 시작일 -> todo 스케쥴러 사용하기
    @Column(nullable = false)
    private LocalDateTime startDate;
    //쿠폰 만료일
    @Column(nullable = false)
    private LocalDateTime expiredDate;

    private Coupon(String name, int discountAmount, int quantity, int availableCount, LocalDateTime startDate, LocalDateTime expiredDate) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.quantity = quantity;
        this.availableCount = availableCount;
        this.startDate = startDate;
        this.expiredDate = expiredDate;
    }

    public static Coupon createCoupon(String name, int discountAmount, int quantity, int availableCount, LocalDateTime startDate, LocalDateTime expiredDate) {
        return new Coupon(name, discountAmount, quantity, availableCount, startDate, expiredDate);
    }

    // ==비즈니스 로직==

    //쿠폰 사용 기간 검증
    public boolean isAvailable(LocalDateTime now) {
        return now.isAfter(startDate) && now.isBefore(expiredDate);
    }

    // 수량 감소
    public void decreaseQuantity() {
        if (this.quantity <= 0) {
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }
        this.quantity -= 1;
    }

    //발급 가능한 수량인지 확인
    public boolean canIssue() {
        return this.quantity > 0;
    }

    //쿠폰 수정
    public void updateCoupon(int availableCount, int quantity) {
        this.availableCount = availableCount;
        this.quantity = quantity;
    }
}
