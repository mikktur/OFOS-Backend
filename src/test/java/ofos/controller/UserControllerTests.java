package ofos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ofos.controller.UserController;
import ofos.dto.CreateUserRequestDTO;
import ofos.entity.UserEntity;
import ofos.security.JwtRequestFilter;
import ofos.security.JwtUtil;
import ofos.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
        userEntity.setUserId(10L);
        userEntity.setUsername("kissa1");
        userEntity.setRole("kissa");

        when(userService.getUserById(10L)).thenReturn(userEntity);

        mvc.perform(get("/api/users/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(userEntity.getRole()))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.id").value(userEntity.getUserId()))
                .andDo(MockMvcResultHandlers.print());


    }

    @SneakyThrows
    @Test
    public void createUserTest() {
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO("kissa10", "kissa10&");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("kissa100");
        userEntity.setRole("kissa");

        when(userService.createUser(createUserRequestDTO)).thenReturn(userEntity);


        // Epäonnistuu aina, koska itse käyttäjää ei koskaan luoda testissä.
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
        userEntity.setUserId(10L);
        userEntity.setUsername(username);
        userEntity.setRole("kissa");

        when(userService.getUserByUsername("kissa1")).thenReturn(userEntity);

        mvc.perform(get("/api/users/username/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(userEntity.getRole()))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.id").value(userEntity.getUserId()))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void getAllUsersTest() throws Exception {
        List<UserEntity> kissat = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        UserEntity userEntity1 = new UserEntity();
        userEntity.setUserId(10L);
        userEntity.setUsername("kissa1");
        userEntity.setRole("kissa");
        userEntity1.setUserId(101L);
        userEntity1.setUsername("kissa12");
        userEntity1.setRole("kissa");
        kissat.add(userEntity);
        kissat.add(userEntity1);

        when(userService.getAllUsers()).thenReturn(kissat);

        mvc.perform(get("/api/users/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(MockMvcResultHandlers.print());

    }


}