package ofos.repository;

import ofos.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.enabled = CASE WHEN u.enabled = true THEN false ELSE true END WHERE u.userId = ?1")
    int updateBanStatus(int id);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.role = 'USER' WHERE u.userId = ?1")
    int setRoleToUser(int userId);

}