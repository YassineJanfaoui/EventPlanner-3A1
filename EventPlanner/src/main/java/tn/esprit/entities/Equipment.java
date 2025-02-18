package tn.esprit.entities;

public class Equipment {
    private int EquipmentId, quantity;
    private String name, category,state;

    public Equipment(int equipmentId, int quantity, String name, String category, String state) {
        EquipmentId = equipmentId;
        this.quantity = quantity;
        this.name = name;
        this.category = category;
        this.state = state;
    }

    public Equipment() {
    }

    public int getEquipmentId() {
        return EquipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        EquipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "EquipmentId=" + EquipmentId +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
