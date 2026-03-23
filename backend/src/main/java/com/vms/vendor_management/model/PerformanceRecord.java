package com.vms.vendor_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_records")
public class PerformanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Evaluation date is required")
    @Column(nullable = false)
    private LocalDate evaluationDate;

    @NotNull(message = "Quality score is required")
    @Min(value = 1, message = "Quality score must be between 1 and 5")
    @Max(value = 5, message = "Quality score must be between 1 and 5")
    @Column(nullable = false)
    private Integer qualityScore;

    @NotNull(message = "Delivery score is required")
    @Min(value = 1, message = "Delivery score must be between 1 and 5")
    @Max(value = 5, message = "Delivery score must be between 1 and 5")
    @Column(nullable = false)
    private Integer deliveryScore;

    @NotNull(message = "Service score is required")
    @Min(value = 1, message = "Service score must be between 1 and 5")
    @Max(value = 5, message = "Service score must be between 1 and 5")
    @Column(nullable = false)
    private Integer serviceScore;

    @Column(nullable = false, precision = 3)
    private Double overallScore;

    @Column(length = 500)
    private String comments;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIgnore
    private Vendor vendor;

    public PerformanceRecord() {
    }

    public PerformanceRecord(Long id, LocalDate evaluationDate, Integer qualityScore, 
                           Integer deliveryScore, Integer serviceScore, String comments, Vendor vendor) {
        this.id = id;
        this.evaluationDate = evaluationDate;
        this.qualityScore = qualityScore;
        this.deliveryScore = deliveryScore;
        this.serviceScore = serviceScore;
        this.comments = comments;
        this.vendor = vendor;
        
        // Calculate overall score
        if (qualityScore != null && deliveryScore != null && serviceScore != null) {
            this.overallScore = (qualityScore + deliveryScore + serviceScore) / 3.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Integer getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Integer qualityScore) {
        this.qualityScore = qualityScore;
        calculateOverallScore();
    }

    public Integer getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(Integer deliveryScore) {
        this.deliveryScore = deliveryScore;
        calculateOverallScore();
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
        calculateOverallScore();
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    private void calculateOverallScore() {
        if (qualityScore != null && deliveryScore != null && serviceScore != null) {
            this.overallScore = (qualityScore + deliveryScore + serviceScore) / 3.0;
        }
    }
}
