package hyos1.myapp.service;

import hyos1.myapp.dto.response.OrderResponse;
import hyos1.myapp.entity.*;
import hyos1.myapp.repository.item.jpa.ItemRepository;
import hyos1.myapp.repository.order.jpa.OrderRepository;
import hyos1.myapp.repository.user.jpa.UserRepository;
import hyos1.myapp.repository.usercoupon.jpa.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserCouponRepository userCouponRepository;


    /**
     * [사용자]쿠폰 없이 주문 생성
     * 한 주문에 한 아이템만 담을 수 있다고 가정
     */
    @Transactional
    public OrderResponse save(Long userId, Long itemId, int count) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getName(), item.getPrice(), count);
        Order order = Order.createOrder(user, orderItem);
        orderRepository.save(order); // cascadeALL로 인해 OrderItem도 같이 저장됨

        return OrderResponse.fromEntity(order);
    }

    /**
     * [사용자]쿠폰 사용하는 주문 생성
     */
    @Transactional
    public OrderResponse saveWithCoupon(Long userId, Long userCouponId, Long itemId, int count) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        //쿠폰 사용가능한지 검증 및 차감
        userCoupon.use(LocalDateTime.now());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getName(), item.getPrice(), count);
        Order order = Order.createOrderWithCoupon(user, userCoupon, orderItem);

        orderRepository.save(order); // cascadeALL로 인해 OrderItem도 같이 저장됨

        return OrderResponse.fromEntity(order);
    }

    /**
     * [사용자]주문 목록 조회
     */
    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findOrdersByUserIdWithCoupon(userId);

        return orders.stream()
                .map(o -> OrderResponse.fromEntity(o))
                .collect(Collectors.toList());
    }

    /**
     * [사용자]단일 주문 조회(OrderItem 포함)
     */
    public OrderResponse findByIdWithOrderItems(Long orderId) {
        Order order = orderRepository.findOrderWithItemsById(orderId).orElseThrow(
                () -> new IllegalArgumentException("주문을 찾을 수 없습니다.")
        );
        return OrderResponse.fromEntity(order);
    }

    /**
     * [관리자]전체 주문 조회
     */
    public List<OrderResponse> findAllWithOrderItems() {
        List<Order> orders = orderRepository.findAllOrdersWithItems();
        return orders.stream()
                .map(o -> OrderResponse.fromEntity(o))
                .collect(Collectors.toList());
    }

    /**
     * [사용자/관리자]주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        order.cancelOrder();
    }
}
