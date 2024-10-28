package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_translations")
@IdClass(TranslationId.class)
public class TranslationEntity {

    @Id
    @Column(name = "Lang")
    private String lang;

    @Id
    @Column(name = "ProductID")
    private int productId;

    @Column(name = "Description")
    private String description;

    @Column(name = "Name")
    private String name;

}
