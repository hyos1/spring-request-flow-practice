package hyos1.myapp.repository.coupon.datajpa;

import hyos1.myapp.entity.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponDataRepository extends JpaRepository<Coupon, Long>, CouponDataRepositoryCustom {

    // 쿠폰 이름이 있는지 확인
    boolean existsByName(String name);
}
