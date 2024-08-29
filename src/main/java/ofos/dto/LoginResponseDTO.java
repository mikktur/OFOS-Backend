package ofos.dto;

public class LoginResponseDTO {
    private boolean success;
    private String username;
    private String message;
    private String token;

    // Constructors, Getters, Setters

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean success,String username, String message, String token) {
        this.success = success;
        this.username = username;
        this.message = message;
        this.token = token;
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


}