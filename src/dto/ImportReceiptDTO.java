package dto;

import java.sql.Timestamp;

public class ImportReceiptDTO {
    private int id;
    private int supplierId;
    private int staffId;
    private double totalAmount;
    private Timestamp createdAt;
    private String supplierName; // For display purposes
    private String staffName; // For display purposes

    public ImportReceiptDTO() {
    }

    public ImportReceiptDTO(int id, int supplierId, int staffId, double totalAmount) {
        this.id = id;
        this.supplierId = supplierId;
        this.staffId = staffId;
        this.totalAmount = totalAmount;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
