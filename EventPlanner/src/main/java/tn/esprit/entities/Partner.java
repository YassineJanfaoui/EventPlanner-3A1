package tn.esprit.entities;

public class Partner {
    private int partnerId;
    private String name;
    private String category;

    public Partner() {
    }

    public Partner(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Partner(int partnerid, String name, String category) {
        this.partnerId = partnerid;
        this.name = name;
        this.category = category;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerid) {
        this.partnerId = partnerid;
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

    @Override
    public String toString() {
        return "Partner{" +
                "partnerid=" + partnerId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
