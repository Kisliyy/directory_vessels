package com.smartgeosystems.vessels_core.models;

public enum Role {
    USER, ADMIN;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
