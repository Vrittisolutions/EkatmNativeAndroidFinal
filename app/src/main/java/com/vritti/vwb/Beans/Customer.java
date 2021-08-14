package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 16-Apr-18.
 */

public class Customer implements Serializable {

    String client_id,client_name,ShiftKeyMasterId;
    String customer_name;
    String  Mobile,EntityContactInfoId;
    String shipToAddress, shipToEmail, shipToMobile, OrderType, OrderTypeMasterId, Latitude, Longitude, City_state_pin_Country;
    String customerId;
    boolean IsShipInvRequired;
    String shipTomasterId="",AddedBy="";

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public Customer() {

    }

    public String getShiftKeyMasterId() {
        return ShiftKeyMasterId;
    }

    public void setShiftKeyMasterId(String shiftKeyMasterId) {
        ShiftKeyMasterId = shiftKeyMasterId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getEntityContactInfoId() {
        return EntityContactInfoId;
    }

    public void setEntityContactInfoId(String entityContactInfoId) {   EntityContactInfoId = entityContactInfoId;   }

    public String getShipToAddress() {   return shipToAddress;   }

    public void setShipToAddress(String shipToAddress) {   this.shipToAddress = shipToAddress;  }

    public String getShipToEmail() {   return shipToEmail;   }

    public void setShipToEmail(String shipToEmail) {  this.shipToEmail = shipToEmail;   }

    public String getShipToMobile() {  return shipToMobile;   }

    public void setShipToMobile(String shipToMobile) {  this.shipToMobile = shipToMobile;  }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderTypeMasterId() {
        return OrderTypeMasterId;
    }

    public void setOrderTypeMasterId(String orderTypeMasterId) {
        OrderTypeMasterId = orderTypeMasterId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCity_state_pin_Country() {
        return City_state_pin_Country;
    }

    public void setCity_state_pin_Country(String city_state_pin_Country) {
        City_state_pin_Country = city_state_pin_Country;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isShipInvRequired() {
        return IsShipInvRequired;
    }

    public void setShipInvRequired(boolean shipInvRequired) {
        IsShipInvRequired = shipInvRequired;
    }

    public String getShipTomasterId() {
        return shipTomasterId;
    }

    public void setShipTomasterId(String shipTomasterId) {
        this.shipTomasterId = shipTomasterId;
    }
}
