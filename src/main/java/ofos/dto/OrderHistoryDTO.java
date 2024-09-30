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

    public OrderHistoryDTO(BigDecimal orderPrice, int quantity, String productName, Date orderDate) {
        this.orderPrice = orderPrice;
        this.quantity = quantity;
        this.productName = productName;
        this.orderDate = orderDate;
    }


}
