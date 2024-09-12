package ofos.repository;

import ofos.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer> {
    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findAll();

    @EntityGraph(attributePaths = {"owner"})
    List<RestaurantEntity> findByOwner_UserId(long userId);

    // Default lazy loading, do not fetch owner eagerly
    Optional<RestaurantEntity> findById(Integer id);

}
