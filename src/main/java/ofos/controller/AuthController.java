package ofos.controller;

import ofos.entity.User;
import ofos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "Authentication successful";
            } else {
                return "Invalid password";
            }
        } else {
            return "User not found";
        }
    }
}
