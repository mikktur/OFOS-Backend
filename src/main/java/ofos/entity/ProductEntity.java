package ofos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import java.util.ArrayList;
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
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranslationEntity> translations = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "Provides",
            joinColumns = @JoinColumn(name = "ProductID"),
            inverseJoinColumns = @JoinColumn(name = "RestaurantID")
    )
    private List<RestaurantEntity> restaurants = new ArrayList<>();
    public ProductEntity() {
    }
    public TranslationEntity getTranslation(String lang) {
        if (translations == null || translations.isEmpty()) {
            return null;
        }
        return translations.stream()
                .filter(t -> t.getId().getLang().equalsIgnoreCase(lang))
                .findFirst()
                .orElse(null);
    }
    public void addRestaurant(RestaurantEntity restaurant) {
        if (restaurants == null) {
            restaurants = new ArrayList<>();
        }
        if (!restaurants.contains(restaurant)) {
            restaurants.add(restaurant);
            restaurant.getProducts().add(this);
        }
    }

    public void addTranslation(TranslationEntity translation) {
        if (translations == null) {
            translations = new ArrayList<>();
        }
        if (!translations.contains(translation)) {
            translations.add(translation);
            translation.setProduct(this);
            translation.getId().setProductId(this.productId);
        }
    }

}