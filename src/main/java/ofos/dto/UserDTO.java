package ofos.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ofos.entity.OrdersEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Integer userId;
    private String username;
    private String role;
    private boolean enabled;

    public UserDTO() {
    }

    public UserDTO(int userId, String username, String role, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.enabled = enabled;
    }
}
