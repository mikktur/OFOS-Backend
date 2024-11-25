package ofos.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactInfoDTO {

    @NotNull
    private String phoneNumber;
    @NotNull
    private String address;
    @NotNull
    private String city;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String postalCode;
    private @NotNull Integer userId;

    public ContactInfoDTO(String phoneNumber, String address, String city,
                          String firstName, String lastName, String email,
                          String postalCode, @NotNull Integer userId) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postalCode = postalCode;
        this.userId = userId;
    }
}
