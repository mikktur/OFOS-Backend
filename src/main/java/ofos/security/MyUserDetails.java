package ofos.security;


import ofos.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Implements the {@link UserDetails} interface to provide core user details required for authentication and authorization in Spring Security.
 *
 * This class acts as a bridge between the application's user data (usually retrieved from a database) and Spring Security's security context.
 * It wraps the user data into a format that Spring Security understands and uses to manage authentication and authorization.
 *
 * The {@link UserDetails} interface provides methods to retrieve user-specific information such as username, password, and roles.
 * This implementation ensures that roles are provided in a format expected by Spring Security and can be used to make authorization decisions.
 */
public class MyUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public MyUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }

    // Custom getter for user ID
    public int getUserId() {
        return userEntity.getUserId();
    }
}