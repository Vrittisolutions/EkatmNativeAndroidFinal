package com.vritti.crm.bean;

import java.io.Serializable;

public class Target implements Serializable {

    String TotalTarget,AchievementAsOnDate,AchievementInPer,
            Backlog,CollectionAmount,TotalOverdueCount,TotalHotOverdueCount,
            TotalColdOverdueCount,OrderCountAchievementAsOnDate,OverdueAchievementAsOnDate,CollectionsAmount,OrderValue;

    public String getTotalTarget() {
        return TotalTarget;
    }

    public void setTotalTarget(String totalTarget) {
        TotalTarget = totalTarget;
    }

    public String getAchievementAsOnDate() {
        return AchievementAsOnDate;
    }

    public void setAchievementAsOnDate(String achievementAsOnDate) {
        AchievementAsOnDate = achievementAsOnDate;
    }

    public String getAchievementInPer() {
        return AchievementInPer;
    }

    public void setAchievementInPer(String achievementInPer) {
        AchievementInPer = achievementInPer;
    }

    public String getBacklog() {
        return Backlog;
    }

    public void setBacklog(String backlog) {
        Backlog = backlog;
    }

    public String getCollectionAmount() {
        return CollectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        CollectionAmount = collectionAmount;
    }

    public String getTotalOverdueCount() {
        return TotalOverdueCount;
    }

    public void setTotalOverdueCount(String totalOverdueCount) {
        TotalOverdueCount = totalOverdueCount;
    }

    public String getTotalHotOverdueCount() {
        return TotalHotOverdueCount;
    }

    public void setTotalHotOverdueCount(String totalHotOverdueCount) {
        TotalHotOverdueCount = totalHotOverdueCount;
    }

    public String getTotalColdOverdueCount() {
        return TotalColdOverdueCount;
    }

    public void setTotalColdOverdueCount(String totalColdOverdueCount) {
        TotalColdOverdueCount = totalColdOverdueCount;
    }

    public String getOrderCountAchievementAsOnDate() {
        return OrderCountAchievementAsOnDate;
    }

    public void setOrderCountAchievementAsOnDate(String orderCountAchievementAsOnDate) {
        OrderCountAchievementAsOnDate = orderCountAchievementAsOnDate;
    }

    public String getOverdueAchievementAsOnDate() {
        return OverdueAchievementAsOnDate;
    }

    public void setOverdueAchievementAsOnDate(String overdueAchievementAsOnDate) {
        OverdueAchievementAsOnDate = overdueAchievementAsOnDate;
    }

    public String getCollectionsAmount() {
        return CollectionsAmount;
    }

    public void setCollectionsAmount(String collectionsAmount) {
        CollectionsAmount = collectionsAmount;
    }

    public String getOrderValue() {
        return OrderValue;
    }

    public void setOrderValue(String orderValue) {
        OrderValue = orderValue;
    }
}
