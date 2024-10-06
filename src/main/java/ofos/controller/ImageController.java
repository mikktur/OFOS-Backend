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

/**
 * This class is used to handle the image requests.
 */
@RestController
@RequestMapping("/images")
public class ImageController extends HttpServlet{


    /**
     * Retrieves an image from the database.
     *
     * @param filename The image file to be retrieved.
     *
     * @return A {@link ResponseEntity} object containing the image.
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/restaurants/products/" + filename);
            return createResponse(file);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves a restaurant logo from the database.
     * @param filename The logo file to be retrieved.
     * @return A {@link ResponseEntity} object containing the logo.
     */
    @GetMapping("/restaurant/{filename:.+}")
    public ResponseEntity<Resource> getRestaurantLogo(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/restaurants/logos/" + filename);
            return createResponse(file);
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

    /**
     * Creates a response entity for the image.
     *
     * @param path The path to the image.
     * @return A {@link ResponseEntity} object containing the image.
     * @throws MalformedURLException If the URL is malformed.
     */
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
