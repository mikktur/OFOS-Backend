package ofos.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    private String oldPassword;
    @Size(min = 6, max = 20
            , message = "Password must be between 6 and 20 characters")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"|,.<>/?].*",
            message = "Password must contain at least one special character")
    private String newPassword;
}
