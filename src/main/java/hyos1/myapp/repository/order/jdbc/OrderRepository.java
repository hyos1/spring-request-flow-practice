package hyos1.myapp.repository.order.jdbc;

import hyos1.myapp.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long orderId, boolean fetchOrderItems);

    List<Order> findAll(boolean fetchOrderItems);

}
