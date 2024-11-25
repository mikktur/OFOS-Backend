package ofos.controller;

import ofos.dto.LoginRequestDTO;
import ofos.dto.LoginResponseDTO;
import ofos.entity.UserEntity;
import ofos.security.JwtUtil;
import ofos.security.MyUserDetails;
import ofos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * This class is used to handle the authentication requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Authenticates the user and generates a token for the user.
     * @param loginRequest The login request object containing the username and password.
     * @return A ResponseEntity object containing the token and status code.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);


            LoginResponseDTO response = new LoginResponseDTO(
                    userDetails.getUserId(),
                    true,
                    userDetails.getUsername(),
                    "Authentication successful",
                    token,
                    role
            );

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new LoginResponseDTO(null, false, null, "Incorrect username or password.", null, null),
                    HttpStatus.UNAUTHORIZED
            );
        }catch (DisabledException e) {
            return new ResponseEntity<>(
                    new LoginResponseDTO(null, false, null, "User is disabled.", null, null),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {

            return new ResponseEntity<>(

                    new LoginResponseDTO(null, false, null, "Access denied: " + e.getMessage(), null, null),
                    HttpStatus.FORBIDDEN
            );
        }
    }
}

