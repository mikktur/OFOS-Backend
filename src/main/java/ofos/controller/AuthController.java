package ofos.controller;

import ofos.dto.LoginRequestDTO;
import ofos.dto.LoginResponseDTO;
import ofos.entity.UserEntity;
import ofos.security.JwtUtil;
import ofos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
/**
 * This class is used to handle the authentication requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticates the user and generates a token for the user.
     *
     * @param loginRequest The login request object containing the username and password.
     * @return A ResponseEntity object containing the token and status code.
     *
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        //retrieve user from database
        UserEntity userEntity = userService.getUserByUsername(loginRequest.getUsername());

        //check if user exists in db and password matches with the user input
        if (userEntity != null && passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            String token = jwtUtil.generateToken(loginRequest.getUsername(), userEntity.getRole());

            //create response object with token
            LoginResponseDTO response = new LoginResponseDTO(userEntity.getUserId(),true, userEntity.getUsername(), "Authentication successful", token, userEntity.getRole());
            System.out.println("User logged in: " + userEntity.getUsername() + " with role: " + userEntity.getRole());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {
            LoginResponseDTO response = new LoginResponseDTO(null,false,null, "Incorrect username or password.", null,null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}

