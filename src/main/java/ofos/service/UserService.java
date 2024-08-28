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

@Service
public class UserService implements UserDetailsService {

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

    // Implementation of loadUserByUsername required by UserDetailsService
    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new MyUserDetails(userEntity); // Return an instance of MyUserDetails which implements UserDetails
    }
}