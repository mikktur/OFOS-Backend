package ofos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofos.dto.DeliveryAddressDTO;
import ofos.security.JwtUtil;
import ofos.service.CustomUserDetailsService;
import ofos.service.DeliveryAddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(DeliveryAddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class DeliveryAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private DeliveryAddressService deliveryAddressService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getDeliveryAddressesTest() throws Exception {
        List<DeliveryAddressDTO> addresses = new ArrayList<>();
        DeliveryAddressDTO deliveryAddressDTO = new DeliveryAddressDTO("Osoite", "Kaupunki", "00410",
                                                1, "Info", true);
        DeliveryAddressDTO deliveryAddressDTO1 = new DeliveryAddressDTO("Osoite2", "Kaupunki", "00410",
                1, "Info", false);
        addresses.add(deliveryAddressDTO);
        addresses.add(deliveryAddressDTO1);

        List<DeliveryAddressDTO> expectedAddress = new ArrayList<>();
        expectedAddress.add(addresses.get(0));
        Mockito.when(deliveryAddressService.getDeliveryAddressesWithDefaultFlag(anyInt())).thenReturn(expectedAddress);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveryaddress/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedAddress)));
    }

    @Test
    void saveDeliveryAddressTest() throws Exception {
        DeliveryAddressDTO deliveryAddressDTO = new DeliveryAddressDTO("Address", "City", "00410", 1, "Info", true);
        String jwt = "Bearer jwtToken"; // Simulate a JWT token
        String dummyUsername = "dummyUser";

        // Mock the JWT extraction to return the dummy username
        Mockito.when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(dummyUsername);

        // Mock the service layer to simulate saving the address
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Saved successfully.");
        Mockito.when(deliveryAddressService.saveDeliveryAddress(Mockito.any(DeliveryAddressDTO.class), Mockito.eq(dummyUsername)))
                .thenReturn(responseEntity);

        // Perform the mock request and check the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveryaddress/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryAddressDTO)) // Convert DTO to JSON string
                        .header("Authorization", jwt)) // Add JWT token to the request header
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(content().string("Saved successfully.")); // Verify the response body
    }

    @Test
    void updateDeliveryAddressTest() throws Exception {
        DeliveryAddressDTO deliveryAddressDTO = new DeliveryAddressDTO("Address", "City", "00410",
                                    1, "Info", true);
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Updated successfully.");
        Mockito.when(deliveryAddressService.updateDeliveryAddress(any(DeliveryAddressDTO.class))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/deliveryaddress/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(deliveryAddressDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDeliveryAddress() throws Exception {
        String jwt = "Bearer jwtToken";
        String dummyUsername = "dummyUser";
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Deleted successfully.");


        Mockito.when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(dummyUsername);
        Mockito.when(deliveryAddressService.deleteDeliveryAddress(anyInt(), eq(dummyUsername))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/deliveryaddress/delete/1")
                .header("Authorization", jwt))
                .andExpect(status().isOk());
    }

    @Test
    void setDefaultDeliveryAddress() throws Exception {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("deliveryAddressId", 1);
        payload.put("userId", 1);
        String jwt = "Bearer jwtToken";
        String dummyUsername = "dummyUser";
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Default address set successfully.");

        Mockito.when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(dummyUsername);
        Mockito.when(deliveryAddressService.setDefaultDeliveryAddress(anyInt(), anyInt(), eq(dummyUsername))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/deliveryaddress/setDefault")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(payload))
                .header("Authorization", jwt))
                .andExpect(status().isOk());
    }


    @Test
    void saveDeliveryAddressFailureTest() throws Exception {
        DeliveryAddressDTO deliveryAddressDTO = new DeliveryAddressDTO("Address", "City", "00410", 1, "Info", true);
        String jwt = "Bearer jwtToken";
        String dummyUsername = "dummyUser";

        Mockito.when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(dummyUsername);
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Save failed.");
        Mockito.when(deliveryAddressService.saveDeliveryAddress(Mockito.any(DeliveryAddressDTO.class), Mockito.eq(dummyUsername)))
                .thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveryaddress/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryAddressDTO))
                        .header("Authorization", jwt))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Save failed."));
    }

    @Test
    void updateDeliveryAddressFailureTest() throws Exception {
        DeliveryAddressDTO deliveryAddressDTO = new DeliveryAddressDTO("Address", "City", "00410", 1, "Info", true);
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed.");
        Mockito.when(deliveryAddressService.updateDeliveryAddress(any(DeliveryAddressDTO.class))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/deliveryaddress/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deliveryAddressDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Update failed."));
    }

    @Test
    void deleteDeliveryAddressFailureTest() throws Exception {
        String jwt = "Bearer jwtToken";
        String dummyUsername = "dummyUser";
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed.");

        Mockito.when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(dummyUsername);
        Mockito.when(deliveryAddressService.deleteDeliveryAddress(anyInt(), eq(dummyUsername))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/deliveryaddress/delete/1")
                        .header("Authorization", jwt))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Delete failed."));
    }

    @Test
    void setDefaultDeliveryAddressFailureTest() throws Exception {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("deliveryAddressId", 1);
        payload.put("userId", 1);
        String jwt = "Bearer jwtToken";
        String dummyUsername = "dummyUser";
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Set default failed.");

        Mockito.when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(dummyUsername);
        Mockito.when(deliveryAddressService.setDefaultDeliveryAddress(anyInt(), anyInt(), eq(dummyUsername))).thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/deliveryaddress/setDefault")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload))
                        .header("Authorization", jwt))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Set default failed."));
    }
}