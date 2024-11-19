package ofos.service;


import ofos.dto.CreateUserRequestDTO;
import ofos.dto.UserDTO;
import ofos.entity.UserEntity;
import ofos.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserByIdTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(222);
        userEntity.setUsername("kissa2");
        userEntity.setPassword("kissa2");
        userEntity.setRole("kissa");

        Mockito.when(userRepository.findByUserId(222)).thenReturn(userEntity);

        UserEntity returnedUserEntity = userService.getUserById(222);

        Assertions.assertEquals(userEntity, returnedUserEntity);
        Assertions.assertEquals(userEntity.getUsername(), returnedUserEntity.getUsername());
    }

    @Test
    public void getUserByUsernameTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(222);
        userEntity.setUsername("kissa2");
        userEntity.setPassword("kissa2");
        userEntity.setRole("kissa");

        Mockito.when(userRepository.findByUsername("kissa2")).thenReturn(userEntity);

        UserEntity returnedUserEntity = userService.getUserByUsername("kissa2");

        Assertions.assertEquals(userEntity, returnedUserEntity);
        Assertions.assertEquals(userEntity.getUserId(), returnedUserEntity.getUserId());
    }

    @Test
    public void getAllUsersTest() {
        List<UserEntity> users = new ArrayList<>();
        UserEntity kissa = new UserEntity(33, "ksis", "ksis", "kissa", true);
        UserEntity kissa1 = new UserEntity(334, "ksis22", "ksis22", "kissa", true);
        users.add(kissa);
        users.add(kissa1);


        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> returnedUsers = userService.getAllUsers();

        Assertions.assertEquals(users.size(), returnedUsers.size(), "Sizes should match");
        Assertions.assertEquals(users.get(0).getUserId(), returnedUsers.get(0).getUserId());
        Assertions.assertEquals(users.get(0).getUsername(), returnedUsers.get(0).getUsername());
        Assertions.assertEquals(users.get(0).getRole(), returnedUsers.get(0).getRole());
        Assertions.assertEquals(users.get(0).isEnabled(), returnedUsers.get(0).isEnabled());

        Assertions.assertEquals(users.get(1).getUserId(), returnedUsers.get(1).getUserId());
        Assertions.assertEquals(users.get(1).getUsername(), returnedUsers.get(1).getUsername());
        Assertions.assertEquals(users.get(1).getRole(), returnedUsers.get(1).getRole());
        Assertions.assertEquals(users.get(1).isEnabled(), returnedUsers.get(1).isEnabled());
    }

    @Test
    public void createUserTest() {
        String username = "testUser";
        String password = "testPassword";

        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO();
        createUserRequestDTO.setUsername(username);
        createUserRequestDTO.setPassword(password);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity returnedUserEntity = userService.createUser(createUserRequestDTO);

        Assertions.assertEquals(userEntity, returnedUserEntity);
        Assertions.assertEquals(userEntity.getUsername(), returnedUserEntity.getUsername());
    }

}