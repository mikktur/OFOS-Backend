package ofos.repository;

import ofos.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<DishEntity, Long> {

    DishEntity findByProductId(int id);

    void deleteByProductId(int id);

}
