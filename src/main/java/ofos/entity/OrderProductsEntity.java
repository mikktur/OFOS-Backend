package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "OrderProducts", schema = "mikt")
public class OrderProductsEntity {

    @EmbeddedId
    private OrderProductsEntityPK id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "OrderID", nullable = false)
    private OrdersEntity order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;

    public OrderProductsEntity() {}

    public OrderProductsEntity(OrdersEntity order, ProductEntity product, Integer quantity) {
        this.id = new OrderProductsEntityPK(order.getOrderId(), product.getProductId());
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderProductsEntity that = (OrderProductsEntity) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}