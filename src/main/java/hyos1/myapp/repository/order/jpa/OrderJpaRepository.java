package hyos1.myapp.repository.order.jpa;

import hyos1.myapp.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderJpaRepository implements OrderRepository {

    private final EntityManager em;

    // [사용자] 주문 생성
    @Override
    public Order save(Order order) {
        em.persist(order);
        return order;
    }

    // [사용자] 본인 주문 단건 조회
    @Override
    public Optional<Order> findByUserIdAndOrderId(Long userId, Long orderId) {
        try {
            Order order = em.createQuery("select o from Order o " +
                            "join fetch o.user u " +
                            "left join fetch o.userCoupon uc " + //쿠폰이 없을 수 있으므로 left join
                            "where o.user.id = :userId " +
                            "and o.id = :orderId", Order.class)
                    .setParameter("userId", userId)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
            return Optional.of(order);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // [사용자] 본인의 주문 전체 조회
    @Override
    public List<Order> findByUserId(Long userId) {
        //toOne 관계만 fetch join, OrderItem은 batch_size 사용
        return em.createQuery("select o from Order o " +
                        "join fetch o.user u " +
                        "left join fetch o.userCoupon uc " + //쿠폰이 없을 수 있으므로 left join
                        "where o.user.id = :userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // [관리자] 주문 단건 조회
    @Override
    public Optional<Order> findWithUserAndCouponById(Long orderId) {
        try {
            Order order = em.createQuery("select o from Order o " +
                            "join fetch o.user u " +
                            "left join fetch o.userCoupon uc " +
                            "where o.id = :orderId", Order.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
            return Optional.of(order);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // [관리자] 주문 전체 조회
    @Override
    public List<Order> findAll() {
        return em.createQuery("select o from Order o " +
                        "join fetch o.user u " +
                        "left join fetch o.userCoupon uc", Order.class)
                .getResultList();
    }
}