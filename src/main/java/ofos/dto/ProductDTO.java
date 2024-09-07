package ofos.dto;

import java.math.BigDecimal;

public class ProductDTO {

    private int productID;
    private String productName;
    private String productDesc;
    private BigDecimal productPrice;
    private String category;
    private String picture;
    private boolean active;
    public ProductDTO(){}

    public ProductDTO(String productName, String productDesc, BigDecimal productPrice, String category, String picture) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.category = category;
        this.picture = picture;
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
}
