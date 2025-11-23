package hyos1.myapp.repository.coupon.datajpa;

import hyos1.myapp.entity.Coupon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CouponDataRepositoryImpl implements CouponDataRepositoryCustom {

    private final EntityManager em;
    @Override
    public Optional<Coupon> findByIdWithLock(Long couponId) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.lock.timeout", 3000);

        Coupon coupon = em.find(Coupon.class, couponId, LockModeType.PESSIMISTIC_WRITE, hints);
        return Optional.ofNullable(coupon);
    }
}
