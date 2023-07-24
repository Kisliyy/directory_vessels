package com.smartgeosystems.directory_vessels.models;

public enum Role {
    USER, ADMIN;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
