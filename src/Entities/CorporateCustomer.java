package Entities;

public class CorporateCustomer extends Customer {
    private String taxNumber;

    public CorporateCustomer(int id, String firstName, String lastName, String email, boolean isApproved, String taxNumber) {
        super(id, firstName, lastName, email, isApproved);
        this.taxNumber = taxNumber;
    }

    public CorporateCustomer(String firstName, String lastName, String email, boolean isApproved, String taxNumber) {
        super(firstName, lastName, email, isApproved);
        this.taxNumber = taxNumber;
    }

    public CorporateCustomer() {
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
}
