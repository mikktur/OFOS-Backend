package ofos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {
    private boolean success;
    private String username;
    private Integer userId;
    private String message;
    private String token;
    private String role;
    // Constructors, Getters, Setters

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Integer userId, boolean success, String username, String message, String token, String role) {
        this.success = success;
        this.username = username;
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.role = role;
    }

    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}