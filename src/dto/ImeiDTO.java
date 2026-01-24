package dto;

import java.sql.Timestamp;

public class ImeiDTO {
    private int id;
    private int skuId;
    private int importReceiptId;
    private String imei;
    private String status; // available, sold, warranty, defective
    private Timestamp createdAt;
    private String skuCode; // For display purposes
    private String productName; // For display purposes

    public ImeiDTO() {
    }

    public ImeiDTO(int id, int skuId, int importReceiptId, String imei, String status) {
        this.id = id;
        this.skuId = skuId;
        this.importReceiptId = importReceiptId;
        this.imei = imei;
        this.status = status;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getImportReceiptId() {
        return importReceiptId;
    }

    public void setImportReceiptId(int importReceiptId) {
        this.importReceiptId = importReceiptId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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
    
    public String getStatusDisplay() {
        switch (status) {
            case "available": return "Còn hàng";
            case "sold": return "Đã bán";
            case "warranty": return "Bảo hành";
            case "defective": return "Lỗi";
            default: return status;
        }
    }
}
