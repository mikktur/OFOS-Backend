package ofos.controller;

import jakarta.servlet.http.HttpServlet;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/images")
public class ImageController extends HttpServlet{

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

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/restaurants/products/" + filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Tällä saa kuvan ladattua, jos frontendi ei osaa näyttää kuvaa URL:ista suoraan.
//    @GetMapping("/{filename:.+}")
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        try {
//            Path file = Paths.get("uploads/restaurants/products/" + filename);
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (MalformedURLException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

}