package ofos.repository;

import ofos.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the {@link UserEntity} class.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //add custom queries here
    UserEntity findByUsername(String username);
    UserEntity findByUserId(int userId);

    @Query("SELECT u.password FROM UserEntity u WHERE u.username = ?1")
    String findPassword(String username);

    @Modifying
    @Query(value = "UPDATE Users SET password = ?1 WHERE username = ?2", nativeQuery = true)
    void updatePassword(String password, String username);
}