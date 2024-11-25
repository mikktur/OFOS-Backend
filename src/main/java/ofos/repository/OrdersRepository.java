package ofos.repository;

import ofos.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for the {@link OrdersEntity} class.
 */
public interface OrdersRepository extends JpaRepository<OrdersEntity, Integer> {

    List<OrdersEntity> findOrdersEntitiesByUser_UserId(int userId);


    @Modifying
    @Transactional
    @Query("UPDATE OrdersEntity o SET o.state = ?2 WHERE o.orderId = ?1")
    void updateByOrderId(int orderID, String status);

    @Query("SELECT o FROM OrdersEntity o WHERE o.user.userId = :userId")
    List<OrdersEntity> findByUserId(@Param("userId") int userId);

    @Query("""
    SELECT o, op, p, t, r
    FROM OrdersEntity o
    JOIN o.orderProducts op
    JOIN op.product p
    JOIN FETCH o.restaurant r
    JOIN FETCH o.user u
    LEFT JOIN p.translations t ON t.id.lang = :language
    WHERE u.username = :username
""")
    List<Object[]> findOrdersByUsername(
            @Param("username") String username,
            @Param("language") String language
    );
}
