package ofos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofos.dto.ContactInfoDTO;
import ofos.entity.ContactInfoEntity;
import ofos.security.JwtUtil;
import ofos.service.ContactInfoService;
import ofos.service.CustomUserDetailsService;
import ofos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContactInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ContactInfoService contactInfoService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @Test
    public void getContactInfoTest() throws Exception {

        ContactInfoEntity contactInfoEntity = new ContactInfoEntity(1, "0505555555", "Osoite 55 B23", "Lontoo",
                                                            "John", "Doe", "john.doe@example.com", "1234567890");

        when(contactInfoService.getContactInfo(anyInt())).thenReturn(contactInfoEntity);

        MvcResult mvcResult = mvc.perform(get("/api/contactinfo/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ContactInfoEntity returnedContactInfo = objectMapper.readValue(contentAsString, ContactInfoEntity.class);

        assertEquals("John", returnedContactInfo.getFirstName());
        assertEquals("Doe", returnedContactInfo.getLastName());
        assertEquals("john.doe@example.com", returnedContactInfo.getEmail());
        assertEquals("1234567890", returnedContactInfo.getPostalCode());
    }

    @Test
    public void updateContactInfoTest() throws Exception {
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO("05055555555", "Osoite 55", "Lontoo", "John",
                                                    "Doe", "john.doe@example.com", "1234567890", 1);

        ResponseEntity<String> responseEntity = ResponseEntity.ok("Contact info updated.");
        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(contactInfoService.updateContactInfo(any(ContactInfoDTO.class), anyString())).thenReturn(responseEntity);

        mvc.perform(post("/api/contactinfo/update")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactInfoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact info updated."));
    }

    @Test
    public void saveInfoTest() throws Exception {
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO("05055555555", "Osoite 55", "Lontoo", "John",
                                                "Doe", "john.doe@example.com", "1234567890", 1);
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Contact info saved for: " + contactInfoDTO.getFirstName());

        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(contactInfoService.saveContactInfo(any(ContactInfoDTO.class), anyString())).thenReturn(responseEntity);

        mvc.perform(post("/api/contactinfo/save")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactInfoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Contact info saved for: John")));
    }


    @Test
    public void getContactInfoNotFoundTest() throws Exception {
        when(contactInfoService.getContactInfo(anyInt())).thenReturn(null);

        mvc.perform(get("/api/contactinfo/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateContactInfoFailureTest() throws Exception {
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO("05055555555", "Osoite 55", "Lontoo", "John",
                "Doe", "john.doe@example.com", "1234567890", 1);

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed.");
        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(contactInfoService.updateContactInfo(any(ContactInfoDTO.class), anyString())).thenReturn(responseEntity);

        mvc.perform(post("/api/contactinfo/update")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactInfoDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Update failed."));
    }

    @Test
    public void saveInfoFailureTest() throws Exception {
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO("05055555555", "Osoite 55", "Lontoo", "John",
                "Doe", "john.doe@example.com", "1234567890", 1);

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Save failed.");
        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(contactInfoService.saveContactInfo(any(ContactInfoDTO.class), anyString())).thenReturn(responseEntity);

        mvc.perform(post("/api/contactinfo/save")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactInfoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Save failed."));
    }

}