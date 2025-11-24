package hyos1.myapp.entity;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void 쿠폰_정상_생성() {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        // when
        Coupon coupon = Coupon.createCoupon("Test", 1000, 10, 1, start, end);

        // then
        assertThat(coupon.getName()).isEqualTo("Test");
        assertThat(coupon.getDiscountAmount()).isEqualTo(1000);
        assertThat(coupon.getQuantity()).isEqualTo(10);
        assertThat(coupon.getAvailableCount()).isEqualTo(1);
        assertThat(coupon.getStartDate()).isEqualTo(start);
        assertThat(coupon.getExpiredDate()).isEqualTo(end);
    }

    @Test
    void 만료일이_시작일보다_이전이면_ClientException_발생() {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusHours(1);

        // when & then
        assertThatThrownBy(() -> Coupon.createCoupon("Test", 1000, 10, 1, start, end))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_DATE_INVALID);
    }

    @Test
    void 발급_가능한_기간인지_검증() {
        // given
        LocalDateTime start = LocalDateTime.now();
        Coupon coupon = Coupon.createCoupon("Test", 1000, 10, 1, start.minusDays(1), start.plusDays(1));

        // when & then
        assertThat(coupon.isAvailableNow(start.minusDays(2))).isFalse();
        assertThat(coupon.isAvailableNow(start)).isTrue();
        assertThat(coupon.isAvailableNow(start.plusDays(2))).isFalse();
    }

    @Test
    void 쿠폰_수량이_없으면_발급x() {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Coupon coupon = Coupon.createCoupon("Test", 1000, 1, 1, start, end);

        // when
        coupon.decreaseQuantity();

        //then
        assertThat(coupon.canIssue()).isFalse();
    }

    @Test
    void 쿠폰_수량_부족하면_예외_발생() {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Coupon coupon = Coupon.createCoupon("Test", 1000, 0, 1, start, end);

        //when & then
        assertThat(coupon.canIssue()).isFalse();
        assertThatThrownBy(() -> coupon.decreaseQuantity())
                .isInstanceOf(ServerException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_SOLD_OUT);
    }

    @Test
    void 쿠폰_수정_정상과_실패_케이스() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.createCoupon("Test", 1000, 5, 1, now, now.plusDays(1));

        // when
        coupon.updateCoupon(2, 3);

        // then
        assertThat(coupon.getAvailableCount()).isEqualTo(2);
        assertThat(coupon.getQuantity()).isEqualTo(3);

        // 실패 케이스
        assertThatThrownBy(() -> coupon.updateCoupon(-1, 3))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_COUNT_INVALID);

        assertThatThrownBy(() -> coupon.updateCoupon(2, -3))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_COUNT_INVALID);
    }
}