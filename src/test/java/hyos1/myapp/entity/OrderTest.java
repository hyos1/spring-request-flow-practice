package hyos1.myapp.entity;

import hyos1.myapp.enums.CouponStatus;
import hyos1.myapp.enums.OrderStatus;
import hyos1.myapp.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {
    private User user;
    private Coupon coupon;
    private UserCoupon userCoupon;
    private Item item;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    @BeforeEach
    void beforeEach() {
        user = User.createUser("userA", "test@naver.com", "1234", UserRole.ROLE_USER);
        item = Item.createItem("itemA", 10000, 10);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime expiredDate = startDate.plusHours(1);
        coupon = Coupon.createCoupon("할로윈", 10000, 1, 1, startDate, expiredDate);
        userCoupon = UserCoupon.createUserCoupon(user, coupon);

        orderItem1 = OrderItem.createOrderItem(item, 2);
        orderItem2 = OrderItem.createOrderItem(item, 3);
    }

    @Test
    void 주문_생성_쿠폰x() {
        //given
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        //when
        Order order = Order.createOrder(user, orderItems, null);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(order.getUserCoupon()).isNull();
        assertThat(order.getOrderItems()).hasSize(2);
        assertThat(order.getFinalPrice()).isEqualTo(50000);
    }
    @Test
    void 주문_생성_쿠폰o() {
        //given
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        //when

        Order order = Order.createOrder(user, orderItems, userCoupon);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(order.getUserCoupon()).isEqualTo(userCoupon);
        assertThat(order.getUserCoupon().getCoupon()).isEqualTo(coupon);
        assertThat(order.getUserCoupon().getCouponStatus()).isEqualTo(CouponStatus.UNUSED);
        assertThat(order.getOrderItems()).hasSize(2);
        assertThat(order.getFinalPrice()).isEqualTo(50000 - order.getUserCoupon().getCoupon().getDiscountAmount());
    }

    @Test
    void 총금액이_음수면_0처리() {
        //given
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        // 원래 가격 5만, 할인금액 6만
        coupon.setDiscountAmount(60000);

        //when
        Order order = Order.createOrder(user, orderItems, userCoupon);

        //then
        // 총 가격보다 할인된 금액이 높아서 음수가 되면 0으로 처리한다.
        assertThat(order.getFinalPrice()).isEqualTo(0);
    }

    @Test
    void 주문취소() {
        //given
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        Order order = Order.createOrder(user, orderItems, null);
        //when
        order.cancelOrder();
        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }
}