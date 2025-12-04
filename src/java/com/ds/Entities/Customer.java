package com.ds.Entities;

public class Customer extends User {

    private boolean isApproved;

    public Customer(int id, String firstName, String lastName, String email, boolean isApproved) {
        super(id, firstName, lastName, email);
        this.isApproved = isApproved;
    }

    public Customer(String firstName, String lastName, String email, boolean isApproved) {
        super(firstName, lastName, email);
        this.isApproved = isApproved;
    }

    public Customer() {
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
