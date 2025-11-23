package hyos1.myapp.repository.order.datajpa;

import hyos1.myapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDataRepository extends JpaRepository<Order, Long> {

    // [사용자] 본인 주문 단건 조회
    @Query("select o from Order o " +
            "join fetch o.user u " +
            "left join fetch o.userCoupon uc " +
            "where o.user.id = :userId " +
            "and o.id = :orderId")
    Optional<Order> findByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

    // [사용자] 본인 주문 전체 조회
    @Query("select o from Order o " +
            "join fetch o.user u " +
            "left join fetch o.userCoupon uc " +
            "where o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);

    // [관리자] 주문 단건 조회
    @Query("select o from Order o " +
            "join fetch o.user u " +
            "left join fetch o.userCoupon uc " +
            "where o.id = :orderId")
    Optional<Order> findWithUserAndCouponById(@Param("orderId") Long orderId);

    // [관리자] 주문 전체 조회
    @Query("select o from Order o " +
            "join fetch o.user u " +
            "left join fetch o.userCoupon uc")
    List<Order> findAll();
}
