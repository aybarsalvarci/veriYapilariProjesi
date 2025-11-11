package Entities;

public class Image extends BaseEntity {

    private int RealEstateId;
    private String path;

    public Image(int id, int realEstateId, String path) {
        super(id);
        RealEstateId = realEstateId;
        this.path = path;
    }

    public int getRealEstateId() {
        return RealEstateId;
    }

    public void setRealEstateId(int realEstateId) {
        RealEstateId = realEstateId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
