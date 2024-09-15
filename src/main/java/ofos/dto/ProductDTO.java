package ofos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ofos.entity.ProductEntity;

import java.math.BigDecimal;

public class ProductDTO {

    private int productID;
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
    public ProductDTO(){}

    public ProductDTO(Integer productID,String productName, String productDesc, BigDecimal productPrice, String category, String picture, boolean active) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.category = category;
        this.picture = picture;
        this.active = active;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public String getCategory() {
        return category;
    }

    public String getPicture() {
        return picture;
    }

    public boolean isActive() {
        return active;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    // Testiä varten.
    public ProductDTO(int productID, String productName, String productDesc, BigDecimal productPrice, String category, String picture) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.category = category;
        this.picture = picture;
    }





}
