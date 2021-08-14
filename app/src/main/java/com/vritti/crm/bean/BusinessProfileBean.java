package com.vritti.crm.bean;

import java.util.ArrayList;

public class BusinessProfileBean {
    String noOfoffices,noOfEmployees,currencId,turnover,address,transacPerDay,productId,currencyName,turnOverDesc;

    ArrayList<BusinessProfileBean> businessProfileBeanArrayList;

    public BusinessProfileBean(ArrayList<BusinessProfileBean> businessProfileBeanArrayList) {
        this.businessProfileBeanArrayList = businessProfileBeanArrayList;
    }

    public BusinessProfileBean() {

    }


    public ArrayList<BusinessProfileBean> getBusinessProfileBeanArrayList() {
        return businessProfileBeanArrayList;
    }

    public void setBusinessProfileBeanArrayList(ArrayList<BusinessProfileBean> businessProfileBeanArrayList) {
        this.businessProfileBeanArrayList = businessProfileBeanArrayList;
    }

    public String getNoOfoffices() {
        return noOfoffices;
    }

    public void setNoOfoffices(String noOfoffices) {
        this.noOfoffices = noOfoffices;
    }

    public String getNoOfEmployees() {
        return noOfEmployees;
    }

    public void setNoOfEmployees(String noOfEmployees) {
        this.noOfEmployees = noOfEmployees;
    }

    public String getCurrencId() {
        return currencId;
    }

    public void setCurrencId(String currencId) {
        this.currencId = currencId;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransacPerDay() {
        return transacPerDay;
    }

    public void setTransacPerDay(String transacPerDay) {
        this.transacPerDay = transacPerDay;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getTurnOverDesc() {
        return turnOverDesc;
    }

    public void setTurnOverDesc(String turnOverDesc) {
        this.turnOverDesc = turnOverDesc;
    }
}
