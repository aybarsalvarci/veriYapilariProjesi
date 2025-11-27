package Entities;

public class IndividualCustomer extends Customer {
    public IndividualCustomer(int id, String firstName, String lastName, String email, boolean isApproved) {
        super(id, firstName, lastName, email, isApproved);
    }

    public IndividualCustomer() {
    }
}
