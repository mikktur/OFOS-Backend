package ofos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {

    private Integer productID;
    @NotBlank(message = "Product name is missing.")
    private String productName;
    @NotBlank(message = "Product description is missing.")
    private String productDesc;
    @NotNull(message = "Product price is missing.")
    private BigDecimal productPrice;
    @NotBlank(message = "Product category is missing.")
    private String category;
    @NotBlank(message = "Picture URL is missing.")
    private String picture;
    private boolean active;
    private String lang;
    public ProductDTO(){}

    public ProductDTO(Integer productID, String productName, String productDesc, BigDecimal productPrice, String category, String picture, boolean active) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.category = category;
        this.picture = picture;
        this.active = active;
    }



    // Testiä varten.
    public ProductDTO(Integer productID, String productName, String productDesc, BigDecimal productPrice, String category, String picture) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.category = category;
        this.picture = picture;
    }





}
