package com.example.chillcup02_ui.data.dto;

import java.util.Date;

public class DiscountDto {
    private String id;
    private String title;
    private String description;
    private String promotionCode;
    private Integer discountPercent;
    private Date expiryDate;
    private Integer minOrder;
    private Boolean isLock;
    private String image;
    private Integer requiredPoints;
    private Boolean isUsed; // null = chưa nhận, true = đã sử dụng, false = chưa sử dụng
    private Date createdAt;
    private Date updatedAt;

    public DiscountDto() {
    }

    public DiscountDto(String id, String title, String description, String promotionCode, 
                      Integer discountPercent, Date expiryDate, Integer minOrder) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.promotionCode = promotionCode;
        this.discountPercent = discountPercent;
        this.expiryDate = expiryDate;
        this.minOrder = minOrder;
        this.isLock = false;
        this.isUsed = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

