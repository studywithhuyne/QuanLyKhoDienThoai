package dto;

public class SkuDTO {
    private int id;
    private int productId;
    private String code;
    private double price;
    private int stock;
    private String productName; // For display purposes
    private String attributes; // For display purposes (combined attribute values)

    public SkuDTO() {
    }

    public SkuDTO(int id, int productId, String code, double price, int stock) {
        this.id = id;
        this.productId = productId;
        this.code = code;
        this.price = price;
        this.stock = stock;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
