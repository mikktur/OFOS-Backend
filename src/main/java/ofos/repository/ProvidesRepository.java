package ofos.repository;

import ofos.entity.ProvidesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for the {@link ProvidesEntity} class.
 */
public interface ProvidesRepository extends JpaRepository<ProvidesEntity, Long> {

    /**
     * Retrieves all products provided by a restaurant of a certain owner.
     *
     * @param name The name of the owner.
     * @return A list of {@link ProvidesEntity} objects containing all products provided by the owner.
     */
    @Query(value = "SELECT * FROM Provides WHERE RestaurantID IN " +
            "( SELECT RestaurantID FROM Restaurants WHERE Owner IN ( SELECT User_ID FROM Users WHERE Username = ?1))"
            , nativeQuery = true)
    List<ProvidesEntity> findProductOwnerByName(String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Provides WHERE ProductID = :productId AND RestaurantID = :restaurantId", nativeQuery = true)
    void deleteByProductIdAndRestaurantId(@Param("productId") int productId, @Param("restaurantId") int restaurantId);

    @Query("SELECT p FROM ProvidesEntity p WHERE p.ProductID = :productId AND p.RestaurantID = :restaurantId")
    ProvidesEntity findByProductIdAndRestaurantId(@Param("productId") int productId, @Param("restaurantId") int restaurantId);
}
