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

@RestController
@RequestMapping("api/contactinfo")
public class ContactInfoController {

    @Autowired
    ContactInfoService contactInfoService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/{userID}")
    public ContactInfoEntity getContactInfo(@PathVariable int userID){
        return contactInfoService.getContactInfo(userID);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateContactInfo(@RequestBody ContactInfoDTO contactInfoDTO, HttpServletRequest request){
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        if (contactInfoService.updateContactInfo(contactInfoDTO, username)) {
            return new ResponseEntity<>(
                    "Contact info updated.",
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Something went wrong.",
                HttpStatus.BAD_REQUEST
        );
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveInfo(@RequestBody ContactInfoDTO dto){
        contactInfoService.saveContactInfo(dto);
        return new ResponseEntity<>(
                "Contact info saved for: " + dto.getFirstName(),
                HttpStatus.OK
        );
    }

}
