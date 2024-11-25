package ofos.controller;


import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.ContactInfoDTO;
import ofos.entity.ContactInfoEntity;
import ofos.security.JwtUtil;
import ofos.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to handle the contact info requests.
 */
@RestController
@RequestMapping("api/contactinfo")
public class ContactInfoController {

    @Autowired
    ContactInfoService contactInfoService;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * Retrieves contact info for a user.
     *
     * @param userID The ID of the user.
     * @return A {@link ResponseEntity} object containing the contact info and status code.
     */
    @GetMapping("/{userID}")
    public ResponseEntity<ContactInfoEntity> getContactInfo(@PathVariable int userID){
        ContactInfoEntity contactInfo = contactInfoService.getContactInfo(userID);
        if (contactInfo != null) {
            return new ResponseEntity<>(contactInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates contact info for a user.
     * @param contactInfoDTO The contact info to be updated.
     * @param request The HTTP request object.
     * @return {@link ResponseEntity} object with and the username of the user.
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateContactInfo(@RequestBody ContactInfoDTO contactInfoDTO, HttpServletRequest request){
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return contactInfoService.updateContactInfo(contactInfoDTO, username);
    }

    /**
     * Saves contact info for a user.
     *
     * @param dto The contact info to be saved.
     * @param request The HTTP request object.
     * @return {@link ResponseEntity} object with data user data.
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveInfo(@RequestBody ContactInfoDTO dto, HttpServletRequest request){
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return contactInfoService.saveContactInfo(dto, username);
    }

}
