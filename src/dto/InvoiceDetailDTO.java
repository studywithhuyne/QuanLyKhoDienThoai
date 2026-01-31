package dto;

/**
 * DTO cho chi tiết phiếu xuất
 */
public class InvoiceDetailDTO {
    private int id;
    private int invoiceId;
    private int skuId;
    private int quantity;
    private Integer imeiId;
    
    // Display fields
    private String skuCode;
    private String productName;
    private double price;
    private String imeiCode;

    public InvoiceDetailDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
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

    public Integer getImeiId() {
        return imeiId;
    }

    public void setImeiId(Integer imeiId) {
        this.imeiId = imeiId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImeiCode() {
        return imeiCode;
    }

    public void setImeiCode(String imeiCode) {
        this.imeiCode = imeiCode;
    }
}
