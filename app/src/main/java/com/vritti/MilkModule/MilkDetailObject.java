package com.vritti.MilkModule;

import java.io.Serializable;

public class MilkDetailObject implements Serializable {
    String ConsigneeName ,ContactNo,Address,Latitude,Longitude,ShipToMasterId,ShipToMasterId1,SequneceNumber,
    tripdetailid,DriverContact,UserName,Vehicleno,TripHeaderId , status , activityId , tripDetailStatus ,tripHeaderStatus ;

    public String getTripDetailStatus() {
        return tripDetailStatus;
    }

    public void setTripDetailStatus(String tripDetailStatus) {
        this.tripDetailStatus = tripDetailStatus;
    }

    public String getTripHeaderStatus() {
        return tripHeaderStatus;
    }

    public void setTripHeaderStatus(String tripHeaderStatus) {
        this.tripHeaderStatus = tripHeaderStatus;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public String getShipToMasterId() {
        return ShipToMasterId;
    }

    public void setShipToMasterId(String shipToMasterId) {
        ShipToMasterId = shipToMasterId;
    }

    public String getShipToMasterId1() {
        return ShipToMasterId1;
    }

    public void setShipToMasterId1(String shipToMasterId1) {
        ShipToMasterId1 = shipToMasterId1;
    }

    public String getSequneceNumber() {
        return SequneceNumber;
    }

    public void setSequneceNumber(String sequneceNumber) {
        SequneceNumber = sequneceNumber;
    }

    public String getTripdetailid() {
        return tripdetailid;
    }

    public void setTripdetailid(String tripdetailid) {
        this.tripdetailid = tripdetailid;
    }

    public String getDriverContact() {
        return DriverContact;
    }

    public void setDriverContact(String driverContact) {
        DriverContact = driverContact;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getVehicleno() {
        return Vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        Vehicleno = vehicleno;
    }

    public String getTripHeaderId() {
        return TripHeaderId;
    }

    public void setTripHeaderId(String tripHeaderId) {
        TripHeaderId = tripHeaderId;
    }
}
