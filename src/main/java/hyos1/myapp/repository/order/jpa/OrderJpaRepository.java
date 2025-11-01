package hyos1.myapp.repository.order.jpa;

import hyos1.myapp.entity.Order;
import hyos1.myapp.repository.order.OrderRepository;
import jakarta.persistence.EntityManager;
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

    //항상 orderItem까지 같이 조회
    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(em.find(Order.class, orderId));
    }

    @Override
    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithOrderItems() {
        return em.createQuery("select o from Order o join fetch o.orderItems oi", Order.class)
                .getResultList();
    }
}
