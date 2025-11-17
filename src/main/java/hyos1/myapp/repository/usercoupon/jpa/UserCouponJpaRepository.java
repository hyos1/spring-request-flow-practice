package hyos1.myapp.repository.usercoupon.jpa;

import hyos1.myapp.entity.UserCoupon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponJpaRepository implements UserCouponRepository {

    private final EntityManager em;

    //[사용자]쿠폰 발급
    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        em.persist(userCoupon);
        return userCoupon;
    }

    //[공용]단건 조회
    @Override
    public Optional<UserCoupon> findById(Long userCouponId) {
        UserCoupon userCoupon = em.find(UserCoupon.class, userCouponId);
        return Optional.ofNullable(userCoupon);
    }

    // 사용자가 발급받은 쿠폰 단건 조회
    @Override
    public Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId) {
        try {
            UserCoupon userCoupon = em.createQuery("select uc from UserCoupon uc " +
                            "where uc.user.id = :userId " +
                            "and uc.coupon.id = :couponId", UserCoupon.class)
                    .setParameter("userId", userId)
                    .setParameter("couponId", couponId)
                    .getSingleResult();
            return Optional.of(userCoupon);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // 사용자가 발급받은 쿠폰 전체 조회
    @Override
    public List<UserCoupon> findAllByUserId(Long userId) {
        return em.createQuery("select uc from UserCoupon uc where uc.user.id = :userId", UserCoupon.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    //[관리자]전체 사용자 쿠폰 목록 조회
    @Override
    public List<UserCoupon> findAll() {
        return em.createQuery("select uc from UserCoupon uc", UserCoupon.class)
                .getResultList();
    }

    // 사용자가 해당 쿠폰 발급 받았었는지 확인
    @Override
    public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
        Long count = em.createQuery("select count(uc) from UserCoupon uc " +
                        "where uc.user.id = :userId " +
                        "and uc.coupon.id = :couponId", Long.class)
                .setParameter("userId", userId)
                .setParameter("couponId", couponId)
                .getSingleResult();
        return count > 0; //이미 존재하면 true 반환
    }
}