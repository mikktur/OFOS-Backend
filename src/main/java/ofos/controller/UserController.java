package ofos.controller;

import ofos.dto.CreateUserRequestDTO;
import ofos.dto.CreateUserResponseDTO;
import ofos.entity.UserEntity;
import ofos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping   // Toimiiko pelkällä oletus pathillä?
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
    public UserEntity getUserById(@PathVariable int id) {
        System.out.println("Entered getUserById method in UserController");
        UserEntity userEntity = userService.getUserById(id);
        System.out.println("User retrieved: " + userEntity.getUsername());
        return userEntity;
    }

    // Pitäisikö kirjautua samalla kun luo tilin?
    @PostMapping("/create")
    public ResponseEntity<CreateUserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequest) {
        try {

            UserEntity createdUser = userService.createUser(createUserRequest);

            System.out.println("User created: " + createdUser.getUsername());
            CreateUserResponseDTO response = new CreateUserResponseDTO(
                    createdUser.getId(),
                    createdUser.getUsername(),
                    "User created successfully",
                    true
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            CreateUserResponseDTO response = new CreateUserResponseDTO(
                    null,
                    null,
                    "User creation failed: " + e.getMessage(),
                    false
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}