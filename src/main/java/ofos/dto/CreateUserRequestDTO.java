package ofos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequestDTO {

    @NotBlank
    @Size(min = 3, max = 20
            , message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank
    @Size(min = 6, max = 20
            , message = "Password must be between 6 and 20 characters")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>/?].*",
            message = "Password must contain at least one special character")
    private String password;

    // Constructors, Getters, Setters
    public CreateUserRequestDTO() {
    }

    public CreateUserRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

