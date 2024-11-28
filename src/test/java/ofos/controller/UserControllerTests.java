package ofos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ofos.dto.CreateUserRequestDTO;
import ofos.dto.UserDTO;
import ofos.entity.UserEntity;
import ofos.security.JwtRequestFilter;
import ofos.security.JwtUtil;
import ofos.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getUserByIdTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(10);
        userEntity.setUsername("kissa1");
        userEntity.setRole("kissa");

        when(userService.getUserById(10)).thenReturn(userEntity);

        mvc.perform(get("/api/users/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(userEntity.getRole()))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.userId").value(userEntity.getUserId()))
                .andDo(MockMvcResultHandlers.print());


    }

    @SneakyThrows
    @Test
    public void createUserTest() {
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO("kissa10", "kissa10&");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("kissa10");
        userEntity.setRole("kissa");


        when(userService.createUser(any(CreateUserRequestDTO.class))).thenReturn(userEntity);



        mvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void getUserByUsernameTest() throws Exception {

        String username = "kissa1";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(10);
        userEntity.setUsername(username);
        userEntity.setRole("kissa");

        when(userService.getUserByUsername("kissa1")).thenReturn(userEntity);

        mvc.perform(get("/api/users/username/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(userEntity.getRole()))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.userId").value(userEntity.getUserId()))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void getAllUsersTest() throws Exception {
        // Mock data: Create UserDTO objects to simulate the service's response
        List<UserDTO> userDTOs = new ArrayList<>();
        UserDTO userDTO1 = new UserDTO(10, "kissa1", "kissa", true);
        UserDTO userDTO2 = new UserDTO(101, "kissa12", "kissa", true);
        userDTOs.add(userDTO1);
        userDTOs.add(userDTO2);

        // Mock the service to return the list of UserDTOs
        when(userService.getAllUsers()).thenReturn(userDTOs);

        // Perform the GET request and validate the response
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(10))
                .andExpect(jsonPath("$[0].username").value("kissa1"))
                .andExpect(jsonPath("$[0].role").value("kissa"))
                .andExpect(jsonPath("$[1].userId").value(101))
                .andExpect(jsonPath("$[1].username").value("kissa12"))
                .andExpect(jsonPath("$[1].role").value("kissa"))
                .andDo(MockMvcResultHandlers.print());

    }



}