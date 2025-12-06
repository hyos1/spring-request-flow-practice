package hyos1.myapp.service;

import hyos1.myapp.repository.coupon.datajpa.CouponDataRepository;
import hyos1.myapp.repository.user.datajpa.UserDataRepository;
import hyos1.myapp.repository.usercoupon.datajpa.UserCouponDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserCouponServiceTest {

    // 순수 jpa
//    private UserCouponJpaRepository userCouponRepository;
//    private UserRepository userRepository;
//    private CouponRepository couponRepository;
    // data jpa
    @Mock
    private UserCouponDataRepository userCouponRepository;
    @Mock
    private UserDataRepository userRepository;
    @Mock
    private CouponDataRepository couponRepository;
    @InjectMocks
    private UserCouponService userCouponService;
//    @Test

}