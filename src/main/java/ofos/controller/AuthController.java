package ofos.controller;

import ofos.entity.UserEntity;
import ofos.security.JwtUtil;
import ofos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password) {
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                String token = jwtUtil.generateToken(username, userEntity.getRole());
                return "Authentication successful. Token: " + token;
            } else {
                return "Invalid password";
            }
        } else {
            return "User not found";
        }
    }
}
