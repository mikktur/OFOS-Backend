package ofos.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class OrderHistoryDTO {

    private BigDecimal orderPrice;

    private int quantity;

    private String productName;

    private Date orderDate;

    private String restaurantName;


    private Integer productId;

    public OrderHistoryDTO(BigDecimal orderPrice, int quantity, String productName, Date orderDate, String restaurantName, Integer productId) {
        this.orderPrice = orderPrice;
        this.quantity = quantity;
        this.productName = productName;
        this.orderDate = orderDate;
        this.restaurantName = restaurantName;
        this.productId = productId;
    }


}
