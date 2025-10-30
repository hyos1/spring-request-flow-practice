package hyos1.myapp.repository.order;

import hyos1.myapp.entity.Order;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long orderId, boolean fetchOrderItems);

    Order findAll();

}
