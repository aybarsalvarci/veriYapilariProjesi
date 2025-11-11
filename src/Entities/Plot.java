package Entities;

public class Plot extends RealEstate{

    protected String zoningStatus;

    public Plot(int id, int customerId, String title, String description, double size, String location, double price, String zoningStatus) {
        super(id, customerId, title, description, size, location, price);
        this.zoningStatus = zoningStatus;
    }

    public Plot() {
    }

    public String getZoningStatus() {
        return zoningStatus;
    }

    public void setZoningStatus(String zoningStatus) {
        this.zoningStatus = zoningStatus;
    }
}
