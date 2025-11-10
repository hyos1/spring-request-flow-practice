package hyos1.myapp.repository.coupon.datajpa;

import hyos1.myapp.entity.Coupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@SpringBootTest
@Transactional
class CouponDataRepositoryTest {

    @Autowired
    CouponDataRepository couponRepository;

    @Test
    void save() {
        Coupon coupon = Coupon.createCoupon("할로윈", 10000, 10, 5, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS), LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).plusSeconds(5));
        couponRepository.save(coupon);

        Optional<Coupon> findCoupon = couponRepository.findById(coupon.getId());
        Assertions.assertThat(coupon).isEqualTo(findCoupon.get());

    }
}