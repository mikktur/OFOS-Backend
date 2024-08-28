package ofos.controller;

import ofos.entity.User;
import ofos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        System.out.println("Entered getAllUsers method in UserController");
        List<User> users = userService.getAllUsers();
        System.out.println("Users retrieved: " + users.get(0).getUsername());
        return users;
    }

    @GetMapping("/username/{username}")
    @ResponseBody
    public User getUserByUsername(@PathVariable String username) {
        System.out.println("Entered getUserByUsername method in UserController");
        User user = userService.getUserByUsername(username);
        System.out.println("User retrieved: " + user.getUsername());
        return user;
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Long id) {
        System.out.println("Entered getUserById method in UserController");
        User user = userService.getUserById(id);
        System.out.println("User retrieved: " + user.getUsername());
        return user;
    }

    @PostMapping("/create")
    public User createUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        System.out.println("Entered createUser method in UserController");
        return userService.createUser(username, password, role);
    }

}