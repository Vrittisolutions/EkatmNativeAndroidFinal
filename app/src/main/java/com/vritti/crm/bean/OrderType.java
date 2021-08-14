package com.vritti.crm.bean;

public class OrderType {

    String orderTypeMasterId, description;
    String vWBUsermasterId,userLoginId,userName;
    String seId,seName;
    String PKCampaignId, CampaignName;

    public OrderType() {

    }

    public String getPKCampaignId() {
        return PKCampaignId;
    }

    public void setPKCampaignId(String PKCampaignId) {
        this.PKCampaignId = PKCampaignId;
    }

    public String getCampaignName() {
        return CampaignName;
    }

    public void setCampaignName(String campaignName) {
        CampaignName = campaignName;
    }

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    public String getSeName() {
        return seName;
    }

    public void setSeName(String seName) {
        this.seName = seName;
    }

    public OrderType(String description, String orderTypeMasterId) {
        this.orderTypeMasterId = orderTypeMasterId;
        this.description = description;
    }

    public OrderType(String description, String orderTypeMasterId,String s,String s1) {
        this.orderTypeMasterId = orderTypeMasterId;
        this.description = description;
    }

    public OrderType(String userLoginId, String vWBUsermasterId, String userName) {
        this.userLoginId = userLoginId;
        this.userName = userName;
        this.vWBUsermasterId = vWBUsermasterId;
    }

    public String getvWBUsermasterId() {
        return vWBUsermasterId;
    }

    public void setvWBUsermasterId(String vWBUsermasterId) {
        this.vWBUsermasterId = vWBUsermasterId;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderTypeMasterId() {
        return orderTypeMasterId;
    }

    public void setOrderTypeMasterId(String orderTypeMasterId) {
        this.orderTypeMasterId = orderTypeMasterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

