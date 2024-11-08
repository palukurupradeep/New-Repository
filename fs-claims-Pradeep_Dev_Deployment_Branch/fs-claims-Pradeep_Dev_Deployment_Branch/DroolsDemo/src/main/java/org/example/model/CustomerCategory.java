package org.example.model;

public enum CustomerCategory {

    GENERAL, KIDS, SENIOR_CITIZEN, SUSPENDED, NORMAL, PREMIUM;

    public String getValue() {
        return this.toString();
    }
}
