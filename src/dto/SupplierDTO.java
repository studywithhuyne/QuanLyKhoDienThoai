package dto;

public class SupplierDTO {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    
    public SupplierDTO() {}
    
    public SupplierDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public SupplierDTO(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    public int getID() {
        return id;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    // Hiển thị trong combobox
    @Override
    public String toString() {
        return name;
    }
}
