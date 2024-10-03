package ofos.service;

import ofos.entity.UserEntity;
import ofos.repository.UserRepository;
import ofos.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * This class provides methods to interact with the user data stored in the database.
 * mainly used in the authentication process to retrieve user details.
 */
// Eikö UserService voi implementoida UserDetailsServicea? Tarvitaanko kahta luokkaa hakemaan samaa dataa tietokannasta?
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param username the username identifying the user whose data is required.
     * @return {@link MyUserDetails} object with selected username.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new MyUserDetails(userEntity); // Return an instance of MyUserDetails which implements UserDetails
    }
}