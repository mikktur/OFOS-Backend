package ofos.dto;


public class CreateUserResponseDTO {
    private Integer user_id;
    private String username;
    private String message;
    private boolean success;


    public CreateUserResponseDTO() {
    }

    public CreateUserResponseDTO(Integer id, String username, String message, boolean success) {
        this.user_id = id;
        this.username = username;
        this.message = message;
        this.success = success;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
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