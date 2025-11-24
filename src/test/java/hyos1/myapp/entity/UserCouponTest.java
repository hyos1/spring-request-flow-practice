package hyos1.myapp.entity;

import hyos1.myapp.common.CouponStatus;
import hyos1.myapp.common.UserRole;
import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {

    private User user;
    private Coupon coupon;
    @BeforeEach
    void beforeEach() {
        user = User.createUser("userA", "test@naver.com", "1234", UserRole.ROLE_USER);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime expiredDate = startDate.plusHours(1);
        coupon = Coupon.createCoupon("할로윈", 10000, 1, 1, startDate, expiredDate);
    }

    @Test
    void UserCoupon생성() {
        //given & when
        UserCoupon userCoupon = UserCoupon.createUserCoupon(user, coupon);

        //then
        assertThat(userCoupon.getUser()).isEqualTo(user);
        assertThat(userCoupon.getCoupon()).isEqualTo(coupon);
        assertThat(userCoupon.getCouponStatus()).isEqualTo(CouponStatus.UNUSED);
    }
    @Test
    void 쿠폰_사용가능횟수가_0이면_상태는_USED야한다() {
        //given
        UserCoupon userCoupon = UserCoupon.createUserCoupon(user, coupon);

        //when
        userCoupon.use();

        //then
        assertThat(userCoupon.getAvailableCount()).isEqualTo(0);
        assertThat(userCoupon.getCouponStatus()).isEqualTo(CouponStatus.USED);
    }
    @Test
    void 쿠폰_사용횟수_초과시_예외발생() {
        //given
        UserCoupon userCoupon = UserCoupon.createUserCoupon(user, coupon);

        //then
        userCoupon.use();

        //then
        assertThatThrownBy(() -> userCoupon.checkAvailable())
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_USAGE_EXHAUSTED);
    }
    @Test
    void 쿠폰이_만료됐으면_예외발생() {
        //given
        UserCoupon userCoupon = UserCoupon.createUserCoupon(user, coupon);

        //when
        LocalDateTime now = LocalDateTime.now().plusHours(2);

        //then
        assertThatThrownBy(() -> userCoupon.checkNotExpired(now)).isInstanceOf(ClientException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.COUPON_EXPIRED);
        assertThat(userCoupon.getCouponStatus()).isEqualTo(CouponStatus.EXPIRED);
    }
    @Test
    void 본인_쿠폰이_아닐시_예외발생() {
        //given
        UserCoupon userCoupon = UserCoupon.createUserCoupon(user, coupon);
        user.setId(1L);

        User newUser = User.createUser("newUser", "new@naver.com", "1234", UserRole.ROLE_USER);
        newUser.setId(2L);

        //when & then
        assertThatThrownBy(() -> userCoupon.checkOwner(newUser.getId()))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_NOT_OWNER);
    }
}