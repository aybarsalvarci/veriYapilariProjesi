package Entities;

public class IndividualCustomer extends Customer {
    public String tc;

    public IndividualCustomer(int id, String firstName, String lastName, String email, boolean isApproved, String tc) {
        super(id, firstName, lastName, email, isApproved);
        this.tc = tc;
    }

    public IndividualCustomer(String firstName, String lastName, String email, boolean isApproved, String tc) {
        super(firstName, lastName, email, isApproved);
        this.tc = tc;
    }

    public IndividualCustomer() {
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }
}
