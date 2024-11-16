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

    /**
     * Retrieves all orders by user ID.
     * @param userID The ID of the user.
     * @return A list of {@link OrdersEntity} objects containing all orders related to the user.
     */

    /**
     * Updates the status of an order by its ID.
     * @param orderID The ID of the order.
     * @param status The new status of the order.
     */
    @Modifying
    @Transactional
    @Query("UPDATE OrdersEntity o SET o.state = ?2 WHERE o.orderId = ?1")
    void updateByOrderId(int orderID, String status);

    @Query("SELECT o FROM OrdersEntity o WHERE o.user.userId = :userId")
    List<OrdersEntity> findByUserId(@Param("userId") int userId);
}
