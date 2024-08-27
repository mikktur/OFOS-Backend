package ofos.repository;

import ofos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //add custom queries here
    User findByUsername(String username);
    User findByUserId(Long userId);
}