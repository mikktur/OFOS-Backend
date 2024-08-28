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

    // Method to create a new user
    public UserEntity createUser(String username, String password, String role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password)); // Encode password before saving
        userEntity.setRole(role);
        return userRepository.save(userEntity);
    }

    // Method to retrieve all users
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to retrieve a user by their username
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Method to retrieve a user by their ID
    public UserEntity getUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

}