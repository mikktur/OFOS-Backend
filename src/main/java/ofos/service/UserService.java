package ofos.service;

import ofos.dto.ChangePasswordDTO;
import ofos.dto.CreateUserRequestDTO;
import ofos.entity.UserEntity;
import ofos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param user contains username,password.
     * @return The created {@link UserEntity} object.
     */
    public UserEntity createUser(CreateUserRequestDTO user) {
        System.out.println("Creating user: " + user.getUsername());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password before saving
        System.out.println("User created: " + userEntity.getUsername());
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
    public UserEntity getUserById(int userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public ResponseEntity<String> updatePassword(ChangePasswordDTO changePasswordDTO, String username){
        String hashedPassword = userRepository.findPassword(username);
        System.out.println("Hashed password: " + hashedPassword);
        if (hashedPassword != null){
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), hashedPassword)){
                String newEncodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
                System.out.println("New encoded password: " + newEncodedPassword);
                userRepository.updatePassword(newEncodedPassword, username);
                return new ResponseEntity<>(
                        "Password updated.",
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    "Old password doesn't match.",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return new ResponseEntity<>(
                "User not found.",
                HttpStatus.NOT_FOUND
        );
    }

}