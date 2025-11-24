package hyos1.myapp.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private Item item;

    @BeforeEach
    void beforeEach() {
        item = Item.createItem("itemA", 10000, 10);
    }
    @Test
    void 주문아이템_생성() {
        //given & when
        OrderItem orderItem = OrderItem.createOrderItem(item, 5);

        //then
        assertThat(orderItem.getName()).isEqualTo(item.getName());
        assertThat(orderItem.getOrderPrice()).isEqualTo(item.getPrice());
        assertThat(orderItem.getQuantity()).isEqualTo(5);
    }
    @Test
    void 주문아이템_총가격() {
        //given
        OrderItem orderItem = OrderItem.createOrderItem(item, 5);
        //when
        int totalPrice = orderItem.getTotalPrice();
        //then
        assertThat(totalPrice).isEqualTo(orderItem.getItem().getPrice() * 5);
    }
}