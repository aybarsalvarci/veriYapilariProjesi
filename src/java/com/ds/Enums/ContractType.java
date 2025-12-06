package com.ds.Enums;

public enum ContractType {
    FOR_RENT("Kiralık"),
    FOR_SALE("Satılık");

    private String label;

    ContractType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
