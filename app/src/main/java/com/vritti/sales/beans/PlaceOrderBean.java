package com.vritti.sales.beans;

/**
 * Created by sharvari on 5/9/2016.
 */
public class PlaceOrderBean {
    int Pid;
    String usertype, expectedDateTime, xml1, xml2, isUploaded;

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getExpectedDateTime() {
        return expectedDateTime;
    }

    public void setExpectedDateTime(String expectedDateTime) {
        this.expectedDateTime = expectedDateTime;
    }

    public String getXml1() {
        return xml1;
    }

    public void setXml1(String xml1) {
        this.xml1 = xml1;
    }

    public String getXml2() {
        return xml2;
    }

    public void setXml2(String xml2) {
        this.xml2 = xml2;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

}
