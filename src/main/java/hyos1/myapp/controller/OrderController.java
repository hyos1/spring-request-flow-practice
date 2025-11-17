package hyos1.myapp.controller;

import hyos1.myapp.dto.request.OrderCreateRequest;
import hyos1.myapp.dto.response.OrderResponse;
import hyos1.myapp.entity.AuthUser;
import hyos1.myapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // [사용자] 주문 생성 ✓
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // [사용자] 본인 주문 단건 조회 ✓
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getMyOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal AuthUser authUser) {
        OrderResponse response = orderService.findUserOrderById(authUser.getUserId(), orderId);
        return ResponseEntity.ok(response);
    }

    // [사용자] 본인 주문 전체 조회
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal AuthUser authUser) {
        List<OrderResponse> userOrders = orderService.findUserOrders(authUser.getUserId());
        return ResponseEntity.ok(userOrders);
    }

    // [사용자] 주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal AuthUser authUser) {
        orderService.cancelOrder(authUser.getUserId(), orderId);
        return ResponseEntity.noContent().build();
    }

    // [관리자] 주문 단건 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<OrderResponse> findByIdAdmin(@PathVariable Long orderId) {
        OrderResponse response = orderService.findOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    // [관리자] 주문 전체 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> findAllAdmin() {
        List<OrderResponse> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }
}