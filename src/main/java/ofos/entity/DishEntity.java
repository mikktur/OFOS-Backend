package ofos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "Products", schema = "mikt")
public class DishEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ProductID")
    private int productId;
    @Basic
    @Column(name = "ProductName")
    private String productName;
    @Basic
    @Column(name = "ProductDesc")
    private String productDesc;
    @Basic
    @Column(name = "ProductPrice")
    private BigDecimal productPrice;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

}
