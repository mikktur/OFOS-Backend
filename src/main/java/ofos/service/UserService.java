package ofos.service;

import ofos.entity.UserEntity;
import ofos.repository.UserRepository;
import ofos.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides methods to interact and save the user data stored in the database.
 */
@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user and saves it to the database.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param role The role of the user.
     * @return The created {@link UserEntity} object.
     */
    public UserEntity createUser(String username, String password, String role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password)); // Encode password before saving
        userEntity.setRole(role);
        return userRepository.save(userEntity);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of {@link UserEntity} objects representing all users in the database.
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The {@link UserEntity} object representing the user.
     */
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return The {@link UserEntity} object representing the user.
     */
    public UserEntity getUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

}