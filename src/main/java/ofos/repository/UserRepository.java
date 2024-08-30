package ofos.repository;

import ofos.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //add custom queries here
    UserEntity findByUsername(String username);
    UserEntity findByUserId(int userId);
}