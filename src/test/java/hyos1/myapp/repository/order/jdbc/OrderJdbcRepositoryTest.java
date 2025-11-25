package hyos1.myapp.repository.order.jdbc;

import hyos1.myapp.enums.UserRole;
import hyos1.myapp.entity.Item;
import hyos1.myapp.entity.Order;
import hyos1.myapp.entity.OrderItem;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.item.jdbc.ItemJdbcRepository;
import hyos1.myapp.repository.user.jdbc.UserJdbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderJdbcRepositoryTest {

    @Autowired private OrderJdbcRepository orderRepository;
    @Autowired private UserJdbcRepository userRepository;
    @Autowired private ItemJdbcRepository itemRepository;


    @Test
    void save() {
        //given
        User user = User.createUser("userA", "user@test.com", "1234", UserRole.ROLE_USER);
        userRepository.save(user);

        Item item1 = Item.createItem("item1", 1000, 10);
        Item item2 = Item.createItem("item2", 2000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 2);
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);
        Order order = Order.createOrder(user,orderItems, null);

        //when
        Order savedOrder = orderRepository.save(order);

        //then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    void findById() {
        // given
        User user = User.createUser("testUser", "user@test.com", "1234", UserRole.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("item1", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, 2);
        List<OrderItem> orderItem1 = List.of(orderItem);
        Order order = Order.createOrder(user, orderItem1, null);
        orderRepository.save(order);

        //when
        Optional<Order> findOrder = orderRepository.findById(order.getId());

        //then
        assertThat(findOrder).isPresent();
        assertThat(findOrder.get().getUser().getId()).isEqualTo(user.getId());

        // Order만 가져왔으므로 OrderItem은 비어있어야 한다.
        assertThat(findOrder.get().getOrderItems()).isEmpty();
    }
    @Test
    void findByIdWithOrderItems() {
        // given
        User user = User.createUser("testUser", "user@test.com", "1234", UserRole.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("item1", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, 2);
        List<OrderItem> orderItem1 = List.of(orderItem);
        Order order = Order.createOrder(user, orderItem1, null);
        orderRepository.save(order);
        // when
        Optional<Order> findOrder = orderRepository.findByIdWithOrderItems(order.getId());

        // then
        System.out.println("findOrder = " + findOrder);
        assertThat(findOrder).isPresent();
        assertThat(findOrder.get().getOrderItems()).hasSize(1);
        assertThat(findOrder.get().getUser().getId()).isEqualTo(user.getId());
    }
    @Test
    void findAll_withoutOrderItems() {
        //given
        User user = User.createUser("userA", "a@naver.com", "1234", UserRole.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("itemA", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, 2);
        List<OrderItem> orderItem1 = List.of(orderItem);
        Order order = Order.createOrder(user, orderItem1, null);
        orderRepository.save(order);

        //when
        List<Order> orders = orderRepository.findAll();

        //then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderItems()).isEmpty();
    }

    @Test
    void findAll_withOrderItems() {
        //given
        User user = User.createUser("userA", "a@naver.com", "1234", UserRole.ROLE_USER);
        userRepository.save(user);

        Item item = Item.createItem("itemA", 1000, 10);
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.createOrderItem(item, 2);
        List<OrderItem> orderItem1 = List.of(orderItem);
        Order order = Order.createOrder(user, orderItem1, null);
        orderRepository.save(order);

        //when
        List<Order> orders = orderRepository.findAllWithOrderItems();

        //then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderItems()).hasSize(1);
        assertThat(orders.get(0).getOrderItems().get(0).getName()).isEqualTo("itemA");
    }
}