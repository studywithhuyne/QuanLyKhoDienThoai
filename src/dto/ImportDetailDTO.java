package dto;

/**
 * DTO cho chi tiết phiếu nhập
 */
public class ImportDetailDTO {
    private int id;
    private int importReceiptId;
    private int productId;
    private int skuId;
    private int quantity;
    
    // Display fields
    private String productName;
    private String skuCode;
    private double price;

    public ImportDetailDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImportReceiptId() {
        return importReceiptId;
    }

    public void setImportReceiptId(int importReceiptId) {
        this.importReceiptId = importReceiptId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
