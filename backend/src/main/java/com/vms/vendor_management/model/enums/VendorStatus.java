package com.vms.vendor_management.model.enums;

public enum VendorStatus {
    PENDING_APPROVAL("Pending Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended"),
    BLACKLISTED("Blacklisted"),
    INACTIVE("Inactive");

    private final String displayName;

    VendorStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}