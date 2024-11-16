package ofos.entity;

import java.io.Serializable;
import java.util.Objects;

public class TranslationId implements Serializable {

    private String lang;
    private Integer productId;

    public TranslationId(){}

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TranslationId that = (TranslationId) object;
        return productId == that.productId && Objects.equals(lang, that.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lang, productId);
    }
}
