package dto;

public class AttributeOptionDTO {
    private int id;
    private int attributeId;
    private String value;
    private String attributeName; // For display purposes

    public AttributeOptionDTO() {
    }

    public AttributeOptionDTO(int id, int attributeId, String value) {
        this.id = id;
        this.attributeId = attributeId;
        this.value = value;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
