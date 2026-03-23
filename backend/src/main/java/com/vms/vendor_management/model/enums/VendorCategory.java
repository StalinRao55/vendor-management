package com.vms.vendor_management.model.enums;

public enum VendorCategory {
    IT_SERVICES("IT Services"),
    LOGISTICS("Logistics & Transportation"),
    MATERIALS("Raw Materials"),
    MANUFACTURING("Manufacturing"),
    CONSULTING("Consulting"),
    FACILITIES("Facilities Management"),
    MARKETING("Marketing & Advertising"),
    FINANCIAL("Financial Services"),
    HUMAN_RESOURCES("Human Resources"),
    LEGAL("Legal Services"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education & Training"),
    RETAIL("Retail & E-commerce"),
    HOSPITALITY("Hospitality & Tourism"),
    ENERGY("Energy & Utilities");

    private final String displayName;

    VendorCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}