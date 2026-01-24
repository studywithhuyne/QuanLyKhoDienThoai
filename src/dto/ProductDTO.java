package dto;

import java.time.LocalDateTime;

import javax.swing.JTextField;

public class ProductDTO {
    private int id;
    private int brandId;
    private int categoryId;
    private String name;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    public ProductDTO() {}

    public ProductDTO(int id, int brandId, int categoryId, String name, LocalDateTime createdAt) {
        this.id = id;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
        this.isDeleted = false;
        this.createdAt = createdAt;
    }

    public ProductDTO(int id, int brandId, int categoryId, String name, boolean isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public ProductDTO(int brandId, int categoryId, String name) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
        this.isDeleted = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                '}';
    }
}
