package ofos.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class OrderProductsEntityPK implements Serializable {

    private int orderId;
    private int productId;

    public OrderProductsEntityPK(){};

    public OrderProductsEntityPK(int orderID, int productID) {
        this.orderId = orderID;
        this.productId = productID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderProductsEntityPK that = (OrderProductsEntityPK) object;
        return orderId == that.orderId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
