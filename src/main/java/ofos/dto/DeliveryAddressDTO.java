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
    private String postalCode;
    @NotNull
    private int deliveryAddressId;

    private String info;

    private boolean defaultAddress;

    public DeliveryAddressDTO() {
    }


    public DeliveryAddressDTO(String streetAddress, String city, String postCode, int deliveryAddressID, String info, boolean defaultAddress) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postCode;
        this.deliveryAddressId = deliveryAddressID;
        this.info = info;
        this.defaultAddress = defaultAddress;
    }


}
