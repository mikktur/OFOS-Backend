package ofos.security;


import ofos.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.Collections;


public class MyUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private UserEntity user;

    public MyUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Gets the role of the user.
        String role = user.getRole(); // e.g., "ADMIN", "USER", etc.

        // Wraps the single role in a SimpleGrantedAuthority and return it as a collection.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

}
