package Entities;

public class CorporateCustomer extends Customer {
    public CorporateCustomer(int id, String firstName, String lastName, String email, boolean isApproved) {
        super(id, firstName, lastName, email, isApproved);
    }

    public CorporateCustomer() {
    }
}
