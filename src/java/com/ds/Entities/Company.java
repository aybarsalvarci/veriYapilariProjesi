package com.ds.Entities;

public class Company extends BaseEntity{

    public String title;
    public String location;
    public String logo;

    public Company(int id, String title, String location, String logo) {
        super(id);
        this.title = title;
        this.location = location;
        this.logo = logo;
    }

    public Company(String title, String location, String logo) {
        this.title = title;
        this.location = location;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
