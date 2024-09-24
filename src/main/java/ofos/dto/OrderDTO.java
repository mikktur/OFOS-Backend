package ofos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

    private String state;

    int quantity;

    int productID;

    int deliveryAddressID;

    int restaurantID;



}
