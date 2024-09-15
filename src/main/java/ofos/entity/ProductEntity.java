package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Products", schema = "mikt")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ProductID")
    private Integer productId;

    @Basic
    @Column(name = "ProductName")
    private String productName;
    @Basic
    @Column(name = "ProductDesc")
    private String productDesc;
    @Basic
    @Column(name = "ProductPrice")
    private BigDecimal productPrice;
    @Basic
    @Column(name = "Picture")
    private String picture;
    @Basic
    @Column(name = "Category")
    private String category;
    @Basic
    @Column(name = "Active")
    private boolean active;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Provides",
            joinColumns = @JoinColumn(name = "ProductID"),
            inverseJoinColumns = @JoinColumn(name = "RestaurantID")
    )
    private List<RestaurantEntity> restaurants;
    public ProductEntity() {
    }


}