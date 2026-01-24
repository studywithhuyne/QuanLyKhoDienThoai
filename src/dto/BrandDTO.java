package dto;

public class BrandDTO {
	private int id;
	private String name;
	private boolean isDeleted;
	
	public BrandDTO() {}
	
	public BrandDTO(int id, String name) {
		this.id = id;
		this.name = name;
		this.isDeleted = false;
	}
	
	public BrandDTO(int id, String name, boolean isDeleted) {
		this.id = id;
		this.name = name;
		this.isDeleted = isDeleted;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isDeleted() {
		return this.isDeleted;
	}
	
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
