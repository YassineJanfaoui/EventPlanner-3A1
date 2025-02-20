package tn.esprit.entities;

public class Catering {
    private int CateringId, NbrPlates, VenueId;
    private double Pricing;
    private String MenuType,MealSchedule,Beverages;

    public Catering() {
    }


    public Catering(int cateringId, String menuType, int nbrPlates, float pricing, String mealSchedule, String beverages, Integer venueId) {
        this.CateringId = cateringId;
        this.MenuType = menuType;
        this.NbrPlates = nbrPlates;
        this.Pricing = pricing;
        this.MealSchedule = mealSchedule;
        this.Beverages = beverages;
        this.VenueId = venueId;
    }

    public int getCateringId() {
        return CateringId;
    }

    public void setCateringId(int cateringId) {
        CateringId = cateringId;
    }

    public int getNbrPlates() {
        return NbrPlates;
    }

    public void setNbrPlates(int nbrPlates) {
        NbrPlates = nbrPlates;
    }

    public int getVenueId() {
        return VenueId;
    }

    public void setVenueId(int venueId) {
        VenueId = venueId;
    }

    public double getPricing() {
        return Pricing;
    }

    public void setPricing(double pricing) {
        Pricing = pricing;
    }

    public String getMenuType() {
        return MenuType;
    }

    public void setMenuType(String menuType) {
        MenuType = menuType;
    }

    public String getMealSchedule() {
        return MealSchedule;
    }

    public void setMealSchedule(String mealSchedule) {
        MealSchedule = mealSchedule;
    }

    public String getBeverages() {
        return Beverages;
    }

    public void setBeverages(String beverages) {
        Beverages = beverages;
    }

    @Override
    public String toString() {
        return "Catering{" +
                "CateringId=" + CateringId +
                ", NbrPlates=" + NbrPlates +
                ", VenueId=" + VenueId +
                ", Pricing=" + Pricing +
                ", MenuType='" + MenuType + '\'' +
                ", MealSchedule='" + MealSchedule + '\'' +
                ", Beverages='" + Beverages + '\'' +
                '}';
    }
}
