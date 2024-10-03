package ofos.repository;

import ofos.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the {@link RestaurantEntity} class.
 */

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer> {
    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findAll();

    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findByOwner_UserId(long userId);


    Optional<RestaurantEntity> findById(Integer id);

    /**
     * Retrieves all restaurants by category name.
     * @param categoryName The name of the category.
     * @return A list of {@link RestaurantEntity} objects containing all restaurants related to the category.
     */
    @Query("SELECT r FROM RestaurantEntity r JOIN r.categories c WHERE c.categoryName = :categoryName")
    List<RestaurantEntity> findByCategoryName(@Param("categoryName") String categoryName);

    /**
     * Retrieves all restaurants by owner name.
     * @param owner The name of the owner.
     * @return A list of {@link RestaurantEntity} objects containing all restaurants related to the owner.
     */
    @Query(value = "SELECT * FROM Restaurants WHERE Owner = " +
            "(SELECT User_ID FROM Users WHERE username = ?1)",
            nativeQuery = true)
    List<RestaurantEntity> findRestaurantByOwnerName(String owner);

}
