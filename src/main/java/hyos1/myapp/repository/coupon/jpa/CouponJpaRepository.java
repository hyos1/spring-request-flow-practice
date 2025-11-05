package hyos1.myapp.repository.coupon.jpa;

import hyos1.myapp.entity.Coupon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public Optional<Coupon> findByIdWithLock(Long couponId) {
        Coupon coupon = em.find(Coupon.class, couponId,
                LockModeType.PESSIMISTIC_WRITE);
        return Optional.ofNullable(coupon);
    }

    @Override
    public List<Coupon> findAll() {
        return em.createQuery("select c from Coupon c", Coupon.class).getResultList();
    }
}
