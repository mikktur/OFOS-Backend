package ofos.controller;

import ofos.entity.UserEntity;
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
    public List<UserEntity> getAllUsers() {
        System.out.println("Entered getAllUsers method in UserController");
        List<UserEntity> userEntities = userService.getAllUsers();
        System.out.println("Users retrieved: " + userEntities.get(0).getUsername());
        return userEntities;
    }

    @GetMapping("/username/{username}")
    @ResponseBody
    public UserEntity getUserByUsername(@PathVariable String username) {
        System.out.println("Entered getUserByUsername method in UserController");
        UserEntity userEntity = userService.getUserByUsername(username);
        System.out.println("User retrieved: " + userEntity.getUsername());
        return userEntity;
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    public UserEntity getUserById(@PathVariable Long id) {
        System.out.println("Entered getUserById method in UserController");
        UserEntity userEntity = userService.getUserById(id);
        System.out.println("User retrieved: " + userEntity.getUsername());
        return userEntity;
    }

    @PostMapping("/create")
    public UserEntity createUser(@RequestParam String username, @RequestParam String password, @RequestParam String role){
        return userService.createUser(username, password, role);
    }

}