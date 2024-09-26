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


    public OrderDTO(String state, int quantity, int productID, int deliveryAddressID, int restaurantID) {
        this.state = state;
        this.quantity = quantity;
        this.productID = productID;
        this.deliveryAddressID = deliveryAddressID;
        this.restaurantID = restaurantID;
    }
}
