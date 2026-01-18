package dto;

import java.time.LocalDateTime;

public class ProductDTO {
    private int id;
    private int brandId;
    private int categoryId;
    private String name;
    private LocalDateTime createdAt;

    // Constructors
    public ProductDTO() {
    }

    public ProductDTO(int id, int brandId, int categoryId, String name, LocalDateTime createdAt) {
        this.id = id;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public ProductDTO(int brandId, int categoryId, String name) {
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
