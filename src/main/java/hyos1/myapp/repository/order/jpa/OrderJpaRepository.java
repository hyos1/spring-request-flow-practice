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


    @Override
    public Order save(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(em.find(Order.class, orderId));
    }

    public Optional<Order> findByIdWithOrderItems(Long orderId) {
        TypedQuery<Order> query = em.createQuery("select o from Order o left join fetch o.orderItems oi " +
                        "where o.id = :orderId", Order.class)
                .setParameter("orderId", orderId);

        try {
            Order order = query.getSingleResult();
            return Optional.of(order);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    // 모든 Order + OrderItem 조회
    public List<Order> findAllWithOrderItems() {
        return em.createQuery("select distinct o from Order o join fetch o.orderItems oi", Order.class)
                .getResultList();
    }
}
