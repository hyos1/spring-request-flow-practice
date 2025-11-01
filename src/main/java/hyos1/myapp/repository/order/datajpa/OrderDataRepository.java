package hyos1.myapp.repository.order.datajpa;

import hyos1.myapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDataRepository extends JpaRepository<Order, Long> {

}
