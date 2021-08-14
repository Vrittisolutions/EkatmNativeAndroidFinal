package com.vritti.crm.bean;

import java.util.ArrayList;

public class CRMDayEndReportBean {

    String Total, OverDue, NewProspect, NewOpportunities, OrderReceivedCount, OrderReceivedValue, OrderLostCount,
            OrderPOValue, CollectionCount, CollectionAmount, OverdueCollectionCount, OverdueCollectionAmount,
            OverdueCollectionCountfor30days, OverdueCollectionAmountfor30days, OverdueCollectionCountfor45Days,
            OverdueCollectionAmountfor45days;
    String OverdueCollectionCountfor90days,OverdueCollectionCountfor90abvdays;
    String OverdueCollectionAmountfor90days,OverdueCollectionAmountfor90abvdays;

    ArrayList<CRMDayEndReportDetailsBean> crmdayEndReportDetailsBeansArrayList;

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getOverDue() {
        return OverDue;
    }

    public void setOverDue(String overDue) {
        OverDue = overDue;
    }

    public String getNewProspect() {
        return NewProspect;
    }

    public void setNewProspect(String newProspect) {
        NewProspect = newProspect;
    }

    public String getNewOpportunities() {
        return NewOpportunities;
    }

    public void setNewOpportunities(String newOpportunities) {
        NewOpportunities = newOpportunities;
    }

    public String getOrderReceivedCount() {
        return OrderReceivedCount;
    }

    public void setOrderReceivedCount(String orderReceivedCount) {
        OrderReceivedCount = orderReceivedCount;
    }

    public String getOrderReceivedValue() {
        return OrderReceivedValue;
    }

    public void setOrderReceivedValue(String orderReceivedValue) {
        OrderReceivedValue = orderReceivedValue;
    }

    public String getOrderLostCount() {
        return OrderLostCount;
    }

    public void setOrderLostCount(String orderLostCount) {
        OrderLostCount = orderLostCount;
    }

    public String getOrderPOValue() {
        return OrderPOValue;
    }

    public void setOrderPOValue(String orderPOValue) {
        OrderPOValue = orderPOValue;
    }

    public String getCollectionCount() {
        return CollectionCount;
    }

    public void setCollectionCount(String collectionCount) {
        CollectionCount = collectionCount;
    }

    public String getCollectionAmount() {
        return CollectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        CollectionAmount = collectionAmount;
    }

    public String getOverdueCollectionCount() {
        return OverdueCollectionCount;
    }

    public void setOverdueCollectionCount(String overdueCollectionCount) {
        OverdueCollectionCount = overdueCollectionCount;
    }

    public String getOverdueCollectionAmount() {
        return OverdueCollectionAmount;
    }

    public void setOverdueCollectionAmount(String overdueCollectionAmount) {
        OverdueCollectionAmount = overdueCollectionAmount;
    }

    public String getOverdueCollectionCountfor30days() {
        return OverdueCollectionCountfor30days;
    }

    public void setOverdueCollectionCountfor30days(String overdueCollectionCountfor30days) {
        OverdueCollectionCountfor30days = overdueCollectionCountfor30days;
    }

    public String getOverdueCollectionAmountfor30days() {
        return OverdueCollectionAmountfor30days;
    }

    public void setOverdueCollectionAmountfor30days(String overdueCollectionAmountfor30days) {
        OverdueCollectionAmountfor30days = overdueCollectionAmountfor30days;
    }

    public String getOverdueCollectionCountfor45Days() {
        return OverdueCollectionCountfor45Days;
    }

    public void setOverdueCollectionCountfor45Days(String overdueCollectionCountfor45Days) {
        OverdueCollectionCountfor45Days = overdueCollectionCountfor45Days;
    }

    public String getOverdueCollectionAmountfor45days() {
        return OverdueCollectionAmountfor45days;
    }

    public void setOverdueCollectionAmountfor45days(String overdueCollectionAmountfor45days) {
        OverdueCollectionAmountfor45days = overdueCollectionAmountfor45days;
    }

    public ArrayList<CRMDayEndReportDetailsBean> getCrmdayEndReportDetailsBeansArrayList() {
        return crmdayEndReportDetailsBeansArrayList;
    }

    public void setCrmdayEndReportDetailsBeansArrayList(ArrayList<CRMDayEndReportDetailsBean> crmdayEndReportDetailsBeansArrayList) {
        this.crmdayEndReportDetailsBeansArrayList = crmdayEndReportDetailsBeansArrayList;
    }

    public String getOverdueCollectionCountfor90days() {
        return OverdueCollectionCountfor90days;
    }

    public void setOverdueCollectionCountfor90days(String overdueCollectionCountfor90days) {
        OverdueCollectionCountfor90days = overdueCollectionCountfor90days;
    }

    public String getOverdueCollectionCountfor90abvdays() {
        return OverdueCollectionCountfor90abvdays;
    }

    public void setOverdueCollectionCountfor90abvdays(String overdueCollectionCountfor90abvdays) {
        OverdueCollectionCountfor90abvdays = overdueCollectionCountfor90abvdays;
    }

    public String getOverdueCollectionAmountfor90days() {
        return OverdueCollectionAmountfor90days;
    }

    public void setOverdueCollectionAmountfor90days(String overdueCollectionAmountfor90days) {
        OverdueCollectionAmountfor90days = overdueCollectionAmountfor90days;
    }

    public String getOverdueCollectionAmountfor90abvdays() {
        return OverdueCollectionAmountfor90abvdays;
    }

    public void setOverdueCollectionAmountfor90abvdays(String overdueCollectionAmountfor90abvdays) {
        OverdueCollectionAmountfor90abvdays = overdueCollectionAmountfor90abvdays;
    }
}
