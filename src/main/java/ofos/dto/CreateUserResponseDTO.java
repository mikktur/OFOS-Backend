package ofos.dto;

import org.springframework.http.HttpStatus;

public class CreateUserResponseDTO {
    private Long user_id; // The ID of the created user
    private String username;
    private String message; // Success message or error message
    private boolean success; // True if the user was created successfully, false otherwise
    // Constructor, getters, and setters

    public CreateUserResponseDTO() {
    }

    public CreateUserResponseDTO(Long id, String username, String message, boolean success) {
        this.user_id = id;
        this.username = username;
        this.message = message;
        this.success = success;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}