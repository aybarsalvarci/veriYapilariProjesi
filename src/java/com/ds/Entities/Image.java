package com.ds.Entities;

public class Image extends BaseEntity {

    private int realEstateId;
    private String path;

    public Image(int id, int realEstateId, String path) {
        super(id);
        this.realEstateId = realEstateId;
        this.path = path;
    }

    public Image(int realEstateId, String path) {
        this.realEstateId = realEstateId;
        this.path = path;
    }

    public Image(){}

    public int getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(int realEstateId) {
        this.realEstateId = realEstateId;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
