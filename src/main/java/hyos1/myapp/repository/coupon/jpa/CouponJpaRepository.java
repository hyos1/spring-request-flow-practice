package hyos1.myapp.repository.coupon.jpa;

import hyos1.myapp.entity.Coupon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponJpaRepository implements CouponRepository {

    private final EntityManager em;

    @Override
    public Coupon save(Coupon coupon) {
        em.persist(coupon);
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        Coupon coupon = em.find(Coupon.class, couponId);
        return Optional.ofNullable(coupon);
    }

    @Override
    public Optional<Coupon> findByIdWithLock(Long couponId) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.lock.timeout", 3000);// 락 대기 시간 3초
        Coupon coupon = em.find(Coupon.class, couponId,
                LockModeType.PESSIMISTIC_WRITE, hints);
        return Optional.ofNullable(coupon);
    }

    @Override
    public List<Coupon> findAll() {
        return em.createQuery("select c from Coupon c", Coupon.class).getResultList();
    }

    @Override
    public boolean existsByName(String name) {
        Long count = em.createQuery("select count(*) from Coupon c where c.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult();
        return count > 0;
    }
}
