package hyos1.myapp.repository.order.jpa;

import hyos1.myapp.common.UserType;
import hyos1.myapp.entity.Item;
import hyos1.myapp.entity.Order;
import hyos1.myapp.entity.OrderItem;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.item.jpa.ItemJpaRepository;
import hyos1.myapp.repository.user.jpa.UserJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderJpaRepositoryTest {

    @Autowired OrderJpaRepository orderRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ItemJpaRepository itemRepository;
    @Autowired
    EntityManager em;

    @Test
    void test() {
        //given
        User user = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);
        userJpaRepository.save(user);

        Item item1 = Item.createItem("itemA", 10000, 10);
        Item item2 = Item.createItem("itemA", 20000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getName(), item1.getPrice(), 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getName(), item2.getPrice(), 2);
        Order order = Order.createOrder(user, orderItem1, orderItem2);

        //when
        Order savedOrder = orderRepository.save(order);
        System.out.println("order = " + order);
        em.flush();
        em.clear();

        //then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(2);
        System.out.println("savedOrder = " + savedOrder);
    }

    @Test
    void findById_withOrderItems() {
        //given
        User user = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);
        userJpaRepository.save(user);

        Item item1 = Item.createItem("itemA", 10000, 10);
        Item item2 = Item.createItem("itemA", 20000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getName(), item1.getPrice(), 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getName(), item2.getPrice(), 2);
        Order order = Order.createOrder(user, orderItem1, orderItem2);
        Order savedOrder = orderRepository.save(order);

        //when
        Optional<Order> findOrder = orderRepository.findOrderWithItemsById(order.getId());

        //then
        assertThat(findOrder).isPresent();
        assertThat(findOrder.get().getOrderItems().size()).isEqualTo(2);
        assertThat(findOrder.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(findOrder.get().getOrderItems().get(0).getItem().getId()).isEqualTo(item1.getId());
    }

    @Test
    void findAll_and_findAllWithOrderItems() {
        //given
        User user = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);
        userJpaRepository.save(user);

        Item item1 = Item.createItem("itemA", 10000, 10);
        Item item2 = Item.createItem("itemA", 20000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getName(), item1.getPrice(), 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getName(), item2.getPrice(), 2);
        Order order = Order.createOrder(user, orderItem1, orderItem2);
        Order savedOrder = orderRepository.save(order);

        em.flush();
        em.clear();

        // when
        System.out.println("==== findAll() ====");
        List<Order> orders1 = orderRepository.findAllOrders();
        System.out.println("orders1.size() = " + orders1.size());

        System.out.println("==== findAllWithOrderItems() ====");
        List<Order> orders2 = orderRepository.findAllOrdersWithItems();
        System.out.println("orders2.size() = " + orders2.size());

        // then
        assertThat(orders1).hasSize(1);
        assertThat(orders2).hasSize(1);

        // N+1 문제 확인용 (Lazy 로딩과 Fetch join 차이)
        System.out.println("==== orderItems in findAll() ====");
        orders1.get(0).getOrderItems().forEach(oi ->
                System.out.println("  item = " + oi.getItem().getName()));

        System.out.println("==== orderItems in findAllWithOrderItems() ====");
        orders2.get(0).getOrderItems().forEach(oi ->
                System.out.println("  item = " + oi.getItem().getName()));
    }

    @Test
    void 연관된_객체_전체조회() {
        //given
        User user = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);
        userJpaRepository.save(user);

        Item item1 = Item.createItem("itemA", 10000, 10);
        Item item2 = Item.createItem("itemA", 20000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getName(), item1.getPrice(), 2);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getName(), item2.getPrice(), 2);
        Order order = Order.createOrder(user, orderItem1, orderItem2);

        //when
        Order savedOrder = orderRepository.save(order);
        //2번째 주문
        //given
        User user2 = User.createUser("userB", "asdf@naver.com", "1234", UserType.USER);
        userJpaRepository.save(user);

        Item item3 = Item.createItem("itemB", 10000, 10);
        Item item4 = Item.createItem("itemB", 20000, 20);
        itemRepository.save(item3);
        itemRepository.save(item4);

        OrderItem orderItem3 = OrderItem.createOrderItem(item3, item3.getName(), item3.getPrice(), 2);
        OrderItem orderItem4 = OrderItem.createOrderItem(item4, item4.getName(), item4.getPrice(), 2);
        Order order2 = Order.createOrder(user, orderItem3, orderItem4);

        //when
        Order savedOrder2 = orderRepository.save(order2);
        em.flush();
        em.clear();

        //then
        List<Order> result = orderRepository.findOrdersByUserIdWithCoupon(user.getId());
        for (Order order1 : result) {
            System.out.println("order = " + order1);
        }
    }
}