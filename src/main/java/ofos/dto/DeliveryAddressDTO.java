package ofos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddressDTO {

    @NotNull
    private String streetAddress;
    @NotNull
    private String city;
    @NotNull
    private String postCode;
    @NotNull
    private int deliveryAddressID;

    public DeliveryAddressDTO(String streetAddress, String city, String postCode, int deliveryAddressID) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.postCode = postCode;
        this.deliveryAddressID = deliveryAddressID;
    }


}
