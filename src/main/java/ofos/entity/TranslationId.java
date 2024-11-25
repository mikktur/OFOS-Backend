package ofos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TranslationId implements Serializable {

    @Column(name = "Lang")
    private String lang;

    @Column(name = "ProductID")
    private Integer productId;

    public TranslationId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationId that = (TranslationId) o;
        return Objects.equals(lang, that.lang) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lang, productId);
    }
}