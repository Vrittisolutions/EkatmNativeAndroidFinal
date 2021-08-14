package com.vritti.MilkModule;

class ObjTripDetail {
    String userMasterId, tripdetailid, TripHeaderId,SequneceNumber, LocationName, ActivityId,shiptomasterid, startLat, startLong, endLat, endLong, ETA_Time, TripDetailStatus, TripHeaderStatus , UserName;

    public String getUserMasterId() {
        return userMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        this.userMasterId = userMasterId;
    }

    public String getTripdetailid() {
        return tripdetailid;
    }

    public void setTripdetailid(String tripdetailid) {
        this.tripdetailid = tripdetailid;
    }

    public String getTripHeaderId() {
        return TripHeaderId;
    }

    public void setTripHeaderId(String tripHeaderId) {
        TripHeaderId = tripHeaderId;
    }

    public String getSequneceNumber() {
        return SequneceNumber;
    }

    public void setSequneceNumber(String sequneceNumber) {
        SequneceNumber = sequneceNumber;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getShiptomasterid() {
        return shiptomasterid;
    }

    public void setShiptomasterid(String shiptomasterid) {
        this.shiptomasterid = shiptomasterid;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }

    public String getETA_Time() {
        return ETA_Time;
    }

    public void setETA_Time(String ETA_Time) {
        this.ETA_Time = ETA_Time;
    }

    public String getTripDetailStatus() {
        return TripDetailStatus;
    }

    public void setTripDetailStatus(String tripDetailStatus) {
        TripDetailStatus = tripDetailStatus;
    }

    public String getTripHeaderStatus() {
        return TripHeaderStatus;
    }

    public void setTripHeaderStatus(String tripHeaderStatus) {
        TripHeaderStatus = tripHeaderStatus;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public ObjTripDetail(String userMasterId, String tripdetailid, String tripHeaderId, String sequneceNumber, String locationName, String activityId, String shiptomasterid, String startLat, String startLong, String endLat, String endLong, String ETA_Time, String tripDetailStatus, String tripHeaderStatus , String userName) {
        this.userMasterId = userMasterId;
        this.tripdetailid = tripdetailid;
        TripHeaderId = tripHeaderId;
        SequneceNumber = sequneceNumber;
        LocationName = locationName;
        ActivityId = activityId;
        this.shiptomasterid = shiptomasterid;
        this.startLat = startLat;
        this.startLong = startLong;
        this.endLat = endLat;
        this.endLong = endLong;
        this.ETA_Time = ETA_Time;
        TripDetailStatus = tripDetailStatus;
        TripHeaderStatus = tripHeaderStatus;
        this.UserName = userName;
    }
}
