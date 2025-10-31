package hyos1.myapp.repository.coupon.jpa;

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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponJpaRepositoryTest {

    @Autowired
    CouponJpaRepository couponRepository;

    @Rollback(value = false)
    @Test
    void save() {
        //given
        Coupon coupon = Coupon.createCoupon("할로윈", 10000, 1, 5, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS), LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).plusSeconds(5));
        //when
        couponRepository.save(coupon);
        //then
        System.out.println("coupon.getId() = " + coupon.getId());
        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        assertThat(coupon).isEqualTo(findCoupon);
    }

    @Test
    void findById() {
        //given
        Coupon coupon = Coupon.createCoupon("할로윈", 10000, 1, 5, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS), LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).plusSeconds(5));

        //when
        couponRepository.save(coupon);

        //then
        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        assertThat(findCoupon.getId()).isEqualTo(coupon.getId());
        assertThat(findCoupon.getName()).isEqualTo(coupon.getName());
        assertThat(findCoupon.getQuantity()).isEqualTo(coupon.getQuantity());
        assertThat(findCoupon.getAvailableCount()).isEqualTo(coupon.getAvailableCount());
        assertThat(coupon).isEqualTo(findCoupon);
    }

    @Test
    void findAll() {
        //given
        Coupon coupon1 = Coupon.createCoupon("할로윈", 10000, 1, 5, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS), LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).plusSeconds(5));
        couponRepository.save(coupon1);
        Coupon coupon2 = Coupon.createCoupon("할로윈", 10000, 1, 5, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS), LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).plusSeconds(5));
        couponRepository.save(coupon2);

        //when
        List<Coupon> result = couponRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        for (Coupon coupon : result) {
            System.out.println("coupon = " + coupon);
        }

    }
}