package hyos1.myapp.repository.order.jpa;

import hyos1.myapp.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    // [사용자] 주문 생성
    Order save(Order order);

    // [사용자] 본인 주문 단건 조회
    Optional<Order> findByUserIdAndOrderId(Long userId, Long orderId);

    // [사용자] 본인 주문 전체 조회
    List<Order> findByUserId(Long userId);

    // [관리자] 주문 단건 조회
    Optional<Order> findById(Long orderId);

    // [관리자]
    List<Order> findAll();
}