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



    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = Paths.get("/app/uploads/restaurants/Products/" + filename);
            return createResponse(file);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/restaurant/{filename:.+}")
    public ResponseEntity<Resource> getRestaurantLogo(@PathVariable String filename) {
        try {
            Path file = Paths.get("/app/uploads/restaurants/Logos/" + filename);
            return createResponse(file);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    private ResponseEntity<Resource> createResponse(Path path) throws MalformedURLException {
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
