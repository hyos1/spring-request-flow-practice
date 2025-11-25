package hyos1.myapp.service;

import hyos1.myapp.enums.OrderStatus;
import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.dto.request.OrderCreateRequest;
import hyos1.myapp.dto.response.OrderResponse;
import hyos1.myapp.entity.*;
import hyos1.myapp.repository.item.datajpa.ItemDataRepository;
import hyos1.myapp.repository.order.datajpa.OrderDataRepository;
import hyos1.myapp.repository.user.datajpa.UserDataRepository;
import hyos1.myapp.repository.usercoupon.datajpa.UserCouponDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hyos1.myapp.dto.request.OrderCreateRequest.OrderItemRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    // 순수 jpa
//    private final OrderJpaRepository orderRepository;
//    private final UserJpaRepository userRepository;
//    private final ItemRepository itemRepository;
//    private final UserCouponRepository userCouponRepository;
    // data jpa
    private final OrderDataRepository orderRepository;
    private final UserDataRepository userRepository;
    private final ItemDataRepository itemRepository;
    private final UserCouponDataRepository userCouponRepository;

    // [사용자] 주문 생성
    @Transactional
    public OrderResponse createOrder(Long userId, OrderCreateRequest request) {

        // 1. 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();

        // 2. 요청한 상품 유무 확인 및 주문상품 생성
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            // 동시에 주문시 재고 문제 생길 수 있으므로 LOCK
            Item item = itemRepository.findByIdWithLock(orderItemRequest.getItemId()).orElseThrow(
                    () -> new ClientException(ErrorCode.ITEM_NOT_FOUND)
            );

            OrderItem orderItem = OrderItem.createOrderItem(item, orderItemRequest.getQuantity());

            // 수량 확인 후 재고 감소
            item.decreaseStock(orderItemRequest.getQuantity());

            orderItems.add(orderItem);
        }

        // 3. 쿠폰 사용 여부 처리
        UserCoupon userCoupon = null;
        if (request.getCouponId() != null) {
            // 유저ID, 쿠폰ID를 사용한 조회
            userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, request.getCouponId())
                    .orElseThrow(() -> new ClientException(ErrorCode.COUPON_NOT_FOUND));

            // 쿠폰 검증 (명시적으로 어떤 검증을 했는지 보여주기 위함)
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

    // [사용자] 주문 단건 조회(OrderItem-> BatchSize 사용)
    public OrderResponse findUserOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByUserIdAndOrderId(userId, orderId)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        return OrderResponse.fromEntity(order);
    }

    // [사용자] 주문 전체 조회(OrderItem-> BatchSize 사용)
    public List<OrderResponse> findUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(o -> OrderResponse.fromEntity(o))
                .collect(Collectors.toList());
    }

    // [관리자]주문 단건 조회
    public OrderResponse findOrderById(Long orderId) {
        Order order = orderRepository.findWithUserAndCouponById(orderId).orElseThrow(
                () -> new ClientException(ErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.fromEntity(order);
    }

    // [관리자]주문 전체 조회
    public List<OrderResponse> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(o -> OrderResponse.fromEntity(o))
                .collect(Collectors.toList());
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByUserIdAndOrderId(userId, orderId).orElseThrow(
                () -> new ClientException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOrderStatus() == OrderStatus.CANCEL) {
            throw new ClientException(ErrorCode.ORDER_ALREADY_CANCELED);
        }

        order.cancelOrder();
    }
}