package hyos1.myapp.service;

import hyos1.myapp.dto.request.OrderCreateRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hyos1.myapp.dto.request.OrderCreateRequest.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserCouponRepository userCouponRepository;


    /**
     * [사용자] 주문 생성
     */
    @Transactional
    public OrderResponse createOrder(Long userId, OrderCreateRequest request) {

        // 1. 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<OrderItem> orderItems = new ArrayList<>();

        // 2. 요청한 상품 유무 확인 및 주문상품 생성
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            Item item = itemRepository.findByIdWithLock(orderItemRequest.getItemId()).orElseThrow(
                    () -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
            );

            OrderItem orderItem = OrderItem.createOrderItem(item, orderItemRequest.getQuantity());

            // 수량 확인 후 재고 감소
            item.decreaseStock(orderItemRequest.getQuantity());

            orderItems.add(orderItem);
        }

        // 3. 쿠폰 사용 여부 처리
        UserCoupon userCoupon = null;
        if (request.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findById(request.getUserCouponId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));

            // 쿠폰 검증 (명시적으로 어떤 검증을 했는지 보여주기 위함)
            userCoupon.checkOwner(userId);
            userCoupon.checkAvailable();
            userCoupon.checkNotExpired(LocalDateTime.now());

            // 검증 통과 후 확인
            userCoupon.use();
        }

        // 4. 주문 생성
        Order order = Order.createOrder(user, orderItems, userCoupon);

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
