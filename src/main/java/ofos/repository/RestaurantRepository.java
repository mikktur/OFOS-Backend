package ofos.repository;

import ofos.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer> {
    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findAll();

    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findByOwner_UserId(long userId);
}
