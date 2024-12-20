package ofos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_translations")
public class TranslationEntity {

    @EmbeddedId
    private TranslationId id = new TranslationId();
    @JsonIgnore
    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;

    @Column(name = "Description")
    private String description;

    @Column(name = "Name")
    private String name;

    public TranslationEntity() {}

    public TranslationEntity(ProductEntity product, String lang, String name, String description) {
        this.product = product;
        this.name = name;
        this.description = description;
        this.id.setProductId(product.getProductId());
        this.id.setLang(lang);
    }

    public TranslationEntity(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public void setLang(String lang) {
        this.id.setLang(lang);
    }

    public void setProductId(int productId) {
        this.id.setProductId(productId);
    }
}

