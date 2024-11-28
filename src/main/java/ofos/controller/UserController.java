package ofos.controller;

import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.ChangePasswordDTO;
import ofos.dto.CreateUserRequestDTO;
import ofos.dto.CreateUserResponseDTO;
import ofos.dto.UserDTO;
import ofos.entity.UserEntity;
import ofos.security.JwtUtil;
import ofos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping   // Toimiiko pelkällä oletus pathillä?
    public List<UserDTO> getAllUsers() {
        System.out.println("Entered getAllUsers method in UserController");
        return userService.getAllUsers();
    }


    @GetMapping("/username/{username}")
    @ResponseBody
    public UserDTO getUserByUsername(@PathVariable String username) {
        System.out.println("Entered getUserByUsername method in UserController");
        UserEntity userEntity = userService.getUserByUsername(username);
        System.out.println("User retrieved: " + userEntity.getUsername());
        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Transform UserEntity to UserDTO
        return new UserDTO(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getRole(),
                userEntity.isEnabled()
        );
    }


    @GetMapping("/id/{id}")
    @ResponseBody
    public UserEntity getUserById(@PathVariable int id) {
        System.out.println("Entered getUserById method in UserController");
        UserEntity userEntity = userService.getUserById(id);
        System.out.println("User retrieved: " + userEntity.getUsername());
        return userEntity;
    }


    /**
     * Creates a new user.
     *
     * @param createUserRequest The request object containing the user data.
     * @return A {@link ResponseEntity} object containing the status code and message.
     */

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequest) {
        try {

            UserEntity createdUser = userService.createUser(createUserRequest);

            System.out.println("User created: " + createdUser.getUsername());
            CreateUserResponseDTO response = new CreateUserResponseDTO(
                    createdUser.getUserId(),
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

    /**
     * Updates user's password.
     * @param changePasswordDTO The request object containing the new password and old password.
     * @param req The HTTP request object for authorization.
     * @return A ResponseEntity object containing the status code and message.
     */
    @PutMapping("/updatePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, HttpServletRequest req) {
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return userService.updatePassword(changePasswordDTO, username);
    }

    /**
     * Deletes a user.
     * @param req The HTTP request object for authorization.
     * @return A ResponseEntity object containing the status code and message.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest req) {
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        if (jwtUtil.extractRole(jwt).equals("OWNER")) {
            return new ResponseEntity<>(
                    "Owner accounts cannot be deleted.",
                    HttpStatus.I_AM_A_TEAPOT
            );
        }
        return userService.deleteUser(username);
    }

    /**
     * Updates the ban status of a user.
     * @param userId The ID of the user to be banned.
     * @return A ResponseEntity object containing the status code and message.
     */
    @PostMapping("/ban/{userId}")
    public ResponseEntity<String> updateBanStatus(@PathVariable int userId) {
        return userService.updateBanStatus(userId);
    }

    @PutMapping("/changerole")
    public ResponseEntity<String> changeRole(@RequestBody UserDTO userDTO) {
        try {
            System.out.println("Entered changeRole method in UserController");
            userService.updateUserRole(userDTO.getUserId(), userDTO.getRole());
            return new ResponseEntity<>(
                    "User's role updated.",
                    HttpStatus.OK
            );
        }catch (UsernameNotFoundException e){
            System.out.println("User not found.");
            return new ResponseEntity<>(
                    "User not found.",
                    HttpStatus.NOT_FOUND
            );
        }



    }
}