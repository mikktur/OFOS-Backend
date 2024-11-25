package ofos.repository;

import ofos.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer> {
    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findAll();

    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findByOwner_UserId(int userId);


    Optional<RestaurantEntity> findByRestaurantID(int id);

    


    @Query("SELECT r FROM RestaurantEntity r JOIN r.categories c WHERE c.categoryName = :categoryName")
    List<RestaurantEntity> findByCategoryName(@Param("categoryName") String categoryName);


    @Query(value = "SELECT * FROM Restaurants WHERE Owner = " +
            "(SELECT User_ID FROM Users WHERE username = ?1)",
            nativeQuery = true)
    List<RestaurantEntity> findRestaurantByOwnerName(String owner);


}
