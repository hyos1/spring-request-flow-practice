package hyos1.myapp.repository.order.jpa;

import hyos1.myapp.common.UserType;
import hyos1.myapp.entity.Item;
import hyos1.myapp.entity.Order;
import hyos1.myapp.entity.OrderItem;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.item.jpa.ItemJpaRepository;
import hyos1.myapp.repository.user.jdbc.UserJdbcRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderJpaRepositoryTest {

    @Autowired OrderJpaRepository orderRepository;
    @Autowired
    UserJdbcRepository userJdbcRepository;
    @Autowired
    ItemJpaRepository itemRepository;
    @Autowired
    EntityManager em;

    @Rollback(value = false)
    @Test
    void test() {
        User user = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);
        userJdbcRepository.save(user);

        Item item = Item.createItem("itemA", 10000, 10);
        itemRepository.save(item);
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getName(), item.getPrice(), 2);
        Order order = Order.createOrder(user, orderItem);
        orderRepository.save(order);
        System.out.println("order = " + order);
        em.flush();
        em.clear();

        Order findOrder = orderRepository.findById(order.getId()).get();
        System.out.println("findOrder.getOrderItems().getClass() = " + findOrder.getOrderItems().getClass());
        System.out.println("findOrder = " + findOrder);
    }

}