package ofos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ofos")
public class OfosApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfosApplication.class, args);
    }
}