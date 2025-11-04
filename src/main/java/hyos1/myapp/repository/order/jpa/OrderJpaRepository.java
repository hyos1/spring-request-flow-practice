package hyos1.myapp.repository.order.jpa;

import hyos1.myapp.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderJpaRepository implements OrderRepository {

    private final EntityManager em;

    // [사용자]주문 저장
    @Override
    public Order save(Order order) {
        em.persist(order);
        return order;
    }

    //단건 주문 조회
    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(em.find(Order.class, orderId));
    }

    //[사용자]단건 주문 + 주문 아이템 포함 조회
    @Override
    public Optional<Order> findOrderWithItemsById(Long orderId) {
        TypedQuery<Order> query = em.createQuery("select distinct o from Order o " +
                        "left join fetch o.orderItems oi " +
                        "where o.id = :orderId", Order.class)
                .setParameter("orderId", orderId);

        try {
            Order order = query.getSingleResult();
            return Optional.of(order);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    //[관리자]전체 주문 조회
    @Override
    public List<Order> findAllOrders() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    //[관리자]전체 주문 + 주문 아이템 즉시 로딩
    @Override
    public List<Order> findAllOrdersWithItems() {
        return em.createQuery("select distinct o from Order o " +
                        "join fetch o.orderItems oi", Order.class)
                .getResultList();
    }

    //[사용자]특정 유저의 주문 목록 조회
    @Override
    public List<Order> findOrdersByUserIdWithCoupon(Long userId) {
        //toOne 관계만 fetch join, OrderItem은 fetch_batch_size
        return em.createQuery("select o from Order o " +
                        "join fetch o.user " +
                        "left join fetch o.userCoupon " + //쿠폰이 없을 수 있으므로 left join
                        "where o.user.id = :userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
