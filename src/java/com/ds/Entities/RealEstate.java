package com.ds.Entities;

import java.math.BigDecimal;

public class RealEstate extends BaseEntity {
    protected int customerId;
    protected String title;
    protected String description;
    protected BigDecimal size;
    protected String location;
    protected BigDecimal price;


    public RealEstate(int id, int customerId, String title, String description, BigDecimal size, String location, BigDecimal price) {
        super(id);
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.size = size;
        this.location = location;
        this.price = price;
    }

    public RealEstate(int customerId, String title, String description, BigDecimal size, String location, BigDecimal price) {
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.size = size;
        this.location = location;
        this.price = price;
    }

    public RealEstate() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
