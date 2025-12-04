package com.ds.Entities;

import com.ds.Enums.Role;

public class SystemUser extends User {

    protected String passwordHash;
    protected Role role;

    public SystemUser(int id, String firstName, String lastName, String email, String passwordHash, Role role) {
        super(id, firstName, lastName, email);
        passwordHash = passwordHash;
        role = role;
    }

    public SystemUser(String firstName, String lastName, String email, String passwordHash, Role role) {
        super(firstName, lastName, email);
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public SystemUser(int id, String passwordHash, Role role) {
        this.setId(id);
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
