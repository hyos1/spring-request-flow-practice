package hyos1.myapp.repository.order.jdbc;

import hyos1.myapp.common.UserType;
import hyos1.myapp.entity.Item;
import hyos1.myapp.entity.Order;
import hyos1.myapp.entity.OrderItem;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.item.jdbc.ItemJdbcRepository;
import hyos1.myapp.repository.user.jdbc.UserJdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderJdbcRepositoryTest {

    @Autowired private OrderJdbcRepository orderRepository;
    @Autowired private UserJdbcRepository userRepository;
    @Autowired private ItemJdbcRepository itemRepository;


    @Test
    void save() {
        //given
        User user = User.createUser("userA", "user@test.com", "1234", UserType.USER);
        userRepository.save(user);

        Item item1 = Item.createItem("item1", 1000, 10);
        Item item2 = Item.createItem("item2", 2000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getName(), item1.getPrice(), 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getName(), item2.getPrice(), 2);

        Order order = Order.createOrder(user, orderItem1, orderItem2);

        //when
        Order savedOrder = orderRepository.save(order);

        //then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    void findByIdOrder() {
        // given
        User user = User.createUser("testUser", "user@test.com", "1234", UserType.USER);
        userRepository.save(user);

        Item item = Item.createItem("item1", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getName(), 1000, 2);
        Order order = Order.createOrder(user, orderItem);
        orderRepository.save(order);

        // when
        Optional<Order> findOrder = orderRepository.findById(order.getId(), true);

        // then
        assertThat(findOrder).isPresent();
        assertThat(findOrder.get().getOrderItems().size()).isEqualTo(1);
        assertThat(findOrder.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(findOrder.get().getOrderItems().get(0).getItem().getId()).isEqualTo(item.getId());
    }
    @Test
    void findAll_withoutOrderItems() {
        //given
        User user = User.createUser("userA", "a@naver.com", "1234", UserType.USER);
        userRepository.save(user);

        Item item = Item.createItem("itemA", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getName(), 1000, 2);
        Order order = Order.createOrder(user, orderItem);
        orderRepository.save(order);

        //when
        List<Order> orders = orderRepository.findAll(false);

        //then
        Order findOrder = orders.get(0);
        System.out.println("findOrder = " + findOrder);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderItems()).isEmpty();
    }

    @Test
    void findAll_withOrderItems() {
        //given
        User user = User.createUser("userA", "a@naver.com", "1234", UserType.USER);
        userRepository.save(user);

        Item item = Item.createItem("itemA", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getName(), 1000, 2);
        Order order = Order.createOrder(user, orderItem);
        orderRepository.save(order);

        //when
        List<Order> orders = orderRepository.findAll(true);

        //then
        Order findOrder = orders.get(0);
        System.out.println("findOrder = " + findOrder);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderItems()).hasSize(1);
        assertThat(orders.get(0).getOrderItems().get(0).getName()).isEqualTo("itemA");

    }
}