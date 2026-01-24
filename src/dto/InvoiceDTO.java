package dto;

import java.sql.Timestamp;

public class InvoiceDTO {
    private int id;
    private int staffId;
    private double totalAmount;
    private Timestamp createdAt;
    private String staffName; // For display purposes

    public InvoiceDTO() {
    }

    public InvoiceDTO(int id, int staffId, double totalAmount) {
        this.id = id;
        this.staffId = staffId;
        this.totalAmount = totalAmount;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
