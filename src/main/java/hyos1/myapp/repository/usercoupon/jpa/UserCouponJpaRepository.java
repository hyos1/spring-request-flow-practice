package hyos1.myapp.repository.usercoupon.jpa;

import hyos1.myapp.entity.UserCoupon;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponJpaRepository implements UserCouponRepository {

    private final EntityManager em;
    @Override
    public Long save(UserCoupon userCoupon) {
        em.persist(userCoupon);
        return userCoupon.getId();
    }

    @Override
    public Optional<UserCoupon> findById(Long userCouponId) {
        UserCoupon userCoupon = em.find(UserCoupon.class, userCouponId);
        return Optional.ofNullable(userCoupon);
    }

    @Override
    public List<UserCoupon> findAll() {
        return em.createQuery("select uc from UserCoupon", UserCoupon.class)
                .getResultList();
    }
}
