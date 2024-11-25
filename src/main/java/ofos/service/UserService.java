package ofos.service;

import ofos.dto.ChangePasswordDTO;
import ofos.dto.CreateUserRequestDTO;
import ofos.dto.UserDTO;
import ofos.entity.DeliveryAddressEntity;
import ofos.entity.RestaurantEntity;
import ofos.entity.UserEntity;
import ofos.entity.UsersAddressEntity;
import ofos.exception.UserNotFoundException;
import ofos.repository.DeliveryAddressRepository;
import ofos.repository.RestaurantRepository;
import ofos.repository.UserRepository;
import ofos.repository.UsersAddressRepository;
import ofos.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides methods to interact and save the user data stored in the database.
 */
@Service
public class UserService {


    private final PasswordEncoder passwordEncoder;


    private final UserRepository userRepository;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private UsersAddressRepository usersAddressRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Creates a new user and saves it to the database.
     * @param user contains username,password.
     * @return The created {@link UserEntity} object.
     */
    @Transactional
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
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUsername(), user.getRole(), user.isEnabled()))
                .collect(Collectors.toList());
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

    /**
     * Updates password for a user.
     * @param changePasswordDTO {@link ChangePasswordDTO} DTO containing old and new password.
     * @param username The username of the user.
     * @return
     */
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

    /**
     * Deletes a user from the database.
     * @param username The username of the user.
     * @return ResponseEntity object with a message and a status code.
     */
    @Transactional
    public ResponseEntity<String> deleteUser(String username){
        UserEntity user = userRepository.findByUsername(username);


        List<UsersAddressEntity> usersAddressEntity = usersAddressRepository.findByUserId(user.getUserId());
        for (UsersAddressEntity uae : usersAddressEntity){
            deliveryAddressRepository.deleteById(uae.getDeliveryAddressId());
        }
        try {
            userRepository.deleteById(user.getUserId());
            return new ResponseEntity<>(
                    "User deleted.",
                    HttpStatus.OK
            );
        } catch (Exception e){
            return new ResponseEntity<>(
                    "Something went wrong",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    /**
     * Updates ban status for a user.
     * @param userId The ID of the user.
     * @return ResponseEntity object with a message and a status code.
     */
    @Transactional
    public ResponseEntity<String> updateBanStatus(int userId){
        int affectedRows = userRepository.updateBanStatus(userId);
        if (affectedRows != 0){
            return new ResponseEntity<>(
                    "User's ban status updated.",
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Something went wrong",
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Updates user role.
     * @param userId The ID of the user.
     * @param role The new role for the user.
     */
    @Transactional
    public void updateUserRole(int userId, String role){

        UserEntity user = userRepository.findByUserId(userId);
        if (user == null){
            throw new UsernameNotFoundException("User not found: " + userId);
        }
        user.setRole(role);
        userRepository.save(user);

    }


}