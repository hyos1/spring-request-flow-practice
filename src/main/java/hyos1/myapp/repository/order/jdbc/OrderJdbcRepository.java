package hyos1.myapp.repository.order.jdbc;

import hyos1.myapp.common.OrderStatus;
import hyos1.myapp.entity.*;
import hyos1.myapp.repository.order.OrderRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert orderInsert;
    private final SimpleJdbcInsert orderItemInsert;

    public OrderJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.orderInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("orders")
                .usingGeneratedKeyColumns("order_id");
        //Order에서 OrderItem까지 같이 저장
        this.orderItemInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_items")
                .usingGeneratedKeyColumns("order_item_id");
    }

    @Override
    public Order save(Order order) {
        //Order 저장
        SqlParameterSource orderParam = new MapSqlParameterSource()
                .addValue("user_id", order.getUser().getId())
                .addValue("order_status", order.getOrderStatus().name())
                .addValue("user_coupon_id",
                        order.getUserCoupon() != null ? order.getUserCoupon().getId() : null)
                .addValue("created_at", LocalDateTime.now());
        Number orderId = orderInsert.executeAndReturnKey(orderParam);
        order.setId(orderId.longValue());
        //OrderItem 저장
        for (OrderItem orderItem : order.getOrderItems()) {
            SqlParameterSource orderItemParam = new MapSqlParameterSource()
                    .addValue("name", orderItem.getName())
                    .addValue("order_price", orderItem.getOrderPrice())
                    .addValue("count", orderItem.getCount())
                    .addValue("item_id", orderItem.getItem().getId())
                    .addValue("order_id", order.getId());
            Number orderItemId = orderItemInsert.executeAndReturnKey(orderItemParam);
            orderItem.setId(orderItemId.longValue());
        }

        return order;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        // 1. Order 기본 정보 조회
        String sql = "select order_id as id, created_at, user_id, user_coupon_id, order_status from orders where order_id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", orderId);
        Order order;

        try {
            order = template.queryForObject(sql, param, orderRowMapper());
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // OrderItems 같이 조회
    public Optional<Order> findByIdWithOrderItems(Long orderId) {
        // 1. Order 기본 정보 조회
        String sql = "select order_id as id, created_at, user_id, user_coupon_id, order_status from orders where order_id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", orderId);
        Order order;

        try {
            order = template.queryForObject(sql, param, orderRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        // OrderItem 조회
        String orderItemSql = "select order_item_id as id, name, order_price, count, item_id from order_items where order_id = :orderId";
        MapSqlParameterSource orderItemParam = new MapSqlParameterSource()
                .addValue("orderId", order.getId());
        List<OrderItem> orderItems = template.query(orderItemSql, orderItemParam, orderItemRowMapper());
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return Optional.of(order);
    }

    @Override
    public List<Order> findAll() {
        // 1. Order 기본 정보 전체 조회
        String sql = "select order_id as id, created_at, user_id, user_coupon_id, order_status from orders";
        List<Order> orders = template.query(sql, orderRowMapper());
        return orders;
    }

    public List<Order> findAllWithOrderItems() {
        // Order 기본 정보 전체 조회
        String sql = "select order_id as id, created_at, user_id, user_coupon_id, order_status from orders";
        List<Order> orders = template.query(sql, orderRowMapper());

        // OrderItem 조회
        String orderItemSql = "select order_item_id as id, name, order_price, count, item_id from order_items where order_id = :orderId";

        for (Order order : orders) {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("orderId", order.getId());
            List<OrderItem> orderItems = template.query(orderItemSql, param, orderItemRowMapper());
            for (OrderItem orderItem : orderItems) {
                order.addOrderItem(orderItem);
            }
        }
        return orders;
    }

    private RowMapper<Order> orderRowMapper() {
        return (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
            order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            // User ID만 세팅, 실제 User는 필요하면 Service쪽에서 세팅하기.
            User user = new User();
            user.setId(rs.getLong("user_id"));
            order.setUser(user);
//            user.addOrder(order); //연관관계 편의 메서드

            Long userCouponId = rs.getLong("user_coupon_id");
            if (!rs.wasNull()) {
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setId(userCouponId);
                order.setUserCoupon(userCoupon);
            }

            return order;
        };
    }

    private RowMapper<OrderItem> orderItemRowMapper() {
        return (rs, rowNum) -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getLong("id"));
            orderItem.setName(rs.getString("name"));
            orderItem.setOrderPrice(rs.getInt("order_price"));
            orderItem.setCount(rs.getInt("count"));

            Item item = new Item();
            item.setId(rs.getLong("item_id"));
            orderItem.setItem(item);

            return orderItem;
        };
    }
}
