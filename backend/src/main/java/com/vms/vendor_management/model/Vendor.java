package com.vms.vendor_management.model;

import com.vms.vendor_management.model.enums.VendorCategory;
import com.vms.vendor_management.model.enums.VendorStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vendor name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Vendor email is required")
    @Email(message = "Provide a valid email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Vendor phone is required")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

    @NotBlank(message = "GST number is required")
    @Column(nullable = false, unique = true)
    private String gstNumber;

    @NotBlank(message = "PAN number is required")
    @Column(nullable = false, unique = true)
    private String panNumber;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 1000)
    private String address;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "Country is required")
    @Column(nullable = false)
    private String country;

    @NotBlank(message = "Pincode is required")
    @Column(nullable = false)
    private String pincode;

    @NotNull(message = "Vendor category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorCategory category;

    @NotBlank(message = "Service type is required")
    @Column(nullable = false)
    private String serviceType;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Payment must be non-negative")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal payment;

    @Column(length = 500)
    private String performance;

    @NotNull(message = "Vendor status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status = VendorStatus.PENDING_APPROVAL;

    @Column(nullable = false)
    private Boolean active = true;

    @Column
    private LocalDate registrationDate;

    @Column
    private LocalDate approvalDate;

    @Column
    private String website;

    @Column
    private String contactPerson;

    @Column
    private String contactDesignation;

    @Column
    private String bankAccountNumber;

    @Column
    private String bankName;

    @Column
    private String bankBranch;

    @Column
    private String ifscCode;

    @Column
    private String documentPath;

    @Column
    private Integer rating = 0;

    @Column
    private Integer totalReviews = 0;

    @Column
    private BigDecimal totalPaymentHistory = BigDecimal.ZERO;

    @Column
    private Integer deliveryPerformance = 0;

    @Column
    private String riskLevel = "LOW";

    @Column
    private String notes;

    public Vendor() {
    }

    public Vendor(Long id, String name, String email, String phone, String serviceType, BigDecimal payment,
                  String performance, Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.serviceType = serviceType;
        this.payment = payment;
        this.performance = performance;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public VendorCategory getCategory() {
        return category;
    }

    public void setCategory(VendorCategory category) {
        this.category = category;
    }

    public VendorStatus getStatus() {
        return status;
    }

    public void setStatus(VendorStatus status) {
        this.status = status;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactDesignation() {
        return contactDesignation;
    }

    public void setContactDesignation(String contactDesignation) {
        this.contactDesignation = contactDesignation;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public BigDecimal getTotalPaymentHistory() {
        return totalPaymentHistory;
    }

    public void setTotalPaymentHistory(BigDecimal totalPaymentHistory) {
        this.totalPaymentHistory = totalPaymentHistory;
    }

    public Integer getDeliveryPerformance() {
        return deliveryPerformance;
    }

    public void setDeliveryPerformance(Integer deliveryPerformance) {
        this.deliveryPerformance = deliveryPerformance;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
