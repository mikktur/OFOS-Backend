package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "OrderProducts", schema = "mikt")
@IdClass(OrderProductsEntityPK.class)
public class OrderProductsEntity {
    @Id
    @Column(name = "OrderID")
    private int orderId;


    @Id
    @Column(name = "ProductID")
    private int productId;

    @Column(name = "quantity")
    private Integer quantity;

    public OrderProductsEntity() {}

    public OrderProductsEntity(int orderId, int productId, Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderProductsEntity that = (OrderProductsEntity) object;
        return orderId == that.orderId && productId == that.productId && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId, quantity);
    }
}
