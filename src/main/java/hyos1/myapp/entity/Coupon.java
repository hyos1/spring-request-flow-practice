package hyos1.myapp.entity;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @ToString.Include
    private String name;

    //할인금액
    @Column(nullable = false)
    @ToString.Include
    private int discountAmount;

    //생성할 수량
    @Column(nullable = false)
    @ToString.Include
    private int quantity;

    //1인당 사용 가능 횟수
    @Column(nullable = false)
    @ToString.Include
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
        // 만료일이 시작일보다 빠르면 오류
        if (expiredDate.isBefore(startDate)) {
            throw new ClientException(ErrorCode.COUPON_DATE_INVALID);
        }
        return new Coupon(name, discountAmount, quantity, availableCount, startDate, expiredDate);
    }

    // ==비즈니스 로직==

    // 쿠폰 발급 가능한 기간인지 검증
    public boolean isAvailableNow(LocalDateTime now) {
        return (now.equals(startDate) || now.isAfter(startDate)) && now.isBefore(expiredDate);
    }

    // 발급 시 수량 감소
    public void decreaseQuantity() {
        if (this.quantity <= 0) {
            throw new ServerException(ErrorCode.COUPON_SOLD_OUT);
        }
        this.quantity -= 1;
    }

    //발급 가능한 수량인지 확인
    public boolean canIssue() {
        return this.quantity > 0;
    }

    //쿠폰 수정
    public void updateCoupon(int availableCount, int quantity) {
        if (availableCount < 0 || quantity < 0) {
            throw new ClientException(ErrorCode.COUPON_COUNT_INVALID);
        }
        this.availableCount = availableCount;
        this.quantity = quantity;
    }
}