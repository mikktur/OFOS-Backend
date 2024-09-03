package ofos.controller;

import jakarta.servlet.http.HttpServlet;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
public class UploadController extends HttpServlet{

    @CrossOrigin(origins = "http://localhost:8001")     // Portti mistä pyynnöt tulee.
    @PostMapping("/upload/user")
    // Ei hyväksy useaa samannimistä kuvaa.
    public void uploadUserPicture(@RequestPart MultipartFile file) {

        try {
            Path path = Paths.get("uploads/users/" + file.getOriginalFilename());   // Voisi generoida tiedoston nimeen. lisämerkkejä

            file.transferTo(path);
            // Heitä file.getOriginalFilename() tietokantaan (UploadServicellä (?))

        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
