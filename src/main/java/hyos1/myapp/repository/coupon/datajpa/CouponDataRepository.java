package hyos1.myapp.repository.coupon.datajpa;

import hyos1.myapp.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponDataRepository extends JpaRepository<Coupon, Long>, CouponDataRepositoryCustom {

    // 쿠폰 이름이 있는지 확인
    boolean existsByName(String name);
}
