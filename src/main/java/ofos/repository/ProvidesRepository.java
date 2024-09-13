package ofos.repository;

import ofos.entity.ProvidesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvidesRepository extends JpaRepository<ProvidesEntity, Long> {


    @Query(value = "SELECT * FROM Provides WHERE RestaurantID IN " +
            "( SELECT RestaurantID FROM Restaurants WHERE Owner IN ( SELECT User_ID FROM Users WHERE Username = ?1))"
            , nativeQuery = true)
    List<ProvidesEntity> findProductOwnerByName(String name);


}
