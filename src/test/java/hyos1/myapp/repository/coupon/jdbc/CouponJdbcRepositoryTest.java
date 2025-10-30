package hyos1.myapp.repository.coupon.jdbc;

import hyos1.myapp.entity.Coupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@Transactional
class CouponJdbcRepositoryTest {

    @Autowired
    private CouponJdbcRepository couponRepository;

    @Rollback(value = false)
    @Test
    void save() {
        Coupon coupon = Coupon.createCoupon("할로윈", 10000, 1, 5, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS), LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).plusSeconds(5));
        couponRepository.save(coupon);
        System.out.println("coupon = " + coupon);
        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();

        Assertions.assertThat(coupon).isEqualTo(findCoupon);
    }

    @Test
    void findById() {
        Coupon coupon = Coupon.createCoupon("할로윈", 10000, 1, 5, LocalDateTime.now(), LocalDateTime.now().plusSeconds(5));
        couponRepository.save(coupon);
        System.out.println("coupon = " + coupon);

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        System.out.println("findCoupon = " + findCoupon);
    }

    @Rollback(value = false)
    @Test
    void findAll() {
        Coupon coupon1 = Coupon.createCoupon("할로윈", 10000, 1, 1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(5));
        Coupon coupon2 = Coupon.createCoupon("추석", 20000, 1, 2, LocalDateTime.now(), LocalDateTime.now().plusSeconds(5));
        Coupon coupon3 = Coupon.createCoupon("설날", 30000, 1, 3, LocalDateTime.now(), LocalDateTime.now().plusSeconds(5));
        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);

        List<Coupon> result = couponRepository.findAll();
        for (Coupon coupon : result) {
            System.out.println("coupon = " + coupon);
        }
    }
}