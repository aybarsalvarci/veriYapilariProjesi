package Entities;

public class Plot extends RealEstate{

    protected String zoningStatus;

    public Plot(int id, int customerId, String title, String description, double size, String location, double price, String zoningStatus) {
        super(id, customerId, title, description, size, location, price);
        this.zoningStatus = zoningStatus;
    }

    public Plot(int customerId, String title, String description, double size, String location, double price, String zoningStatus) {
        super(customerId, title, description, size, location, price);
        this.zoningStatus = zoningStatus;
    }

    public Plot(int id, String zoningStatus) {
        this.setId(id);
        this.zoningStatus = zoningStatus;
    }

    public String getZoningStatus() {
        return zoningStatus;
    }

    public void setZoningStatus(String zoningStatus) {
        this.zoningStatus = zoningStatus;
    }
}
