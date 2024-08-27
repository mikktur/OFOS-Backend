package ofos.controller;

import ofos.entity.User;
import ofos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}