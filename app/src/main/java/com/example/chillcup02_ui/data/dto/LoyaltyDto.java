package com.example.chillcup02_ui.data.dto;

import java.util.Date;
import java.util.List;

public class LoyaltyDto {
    private String userId;
    private Integer totalPoints;
    private List<PointHistory> history;
    private Date createdAt;
    private Date updatedAt;

    public LoyaltyDto() {
    }

    public LoyaltyDto(String userId, Integer totalPoints) {
        this.userId = userId;
        this.totalPoints = totalPoints;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTotalPoints() {
        return totalPoints != null ? totalPoints : 0;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<PointHistory> getHistory() {
        return history;
    }

    public void setHistory(List<PointHistory> history) {
        this.history = history;
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

    public static class PointHistory {
        private String orderId;
        private Integer pointsEarned;
        private Date date;

        public PointHistory() {
        }

        public PointHistory(String orderId, Integer pointsEarned, Date date) {
            this.orderId = orderId;
            this.pointsEarned = pointsEarned;
            this.date = date;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getPointsEarned() {
            return pointsEarned;
        }

        public void setPointsEarned(Integer pointsEarned) {
            this.pointsEarned = pointsEarned;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}

