package hyos1.myapp.controller;

import hyos1.myapp.dto.request.OrderCreateRequest;
import hyos1.myapp.dto.response.OrderResponse;
import hyos1.myapp.entity.AuthUser;
import hyos1.myapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
