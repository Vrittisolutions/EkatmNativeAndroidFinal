package com.vritti.vwb.Beans;

/**
 * Created by Admin-1 on 3/31/2017.
 */

public class GPSMyLocationBean {
    String  GPSID ,
            FKUserMasterId ,
            MobileNo ,
            latitude ,
            longitude ,
            locationName ,
    AddedDT;

    public String getGPSID() {
        return GPSID;
    }

    public void setGPSID(String GPSID) {
        this.GPSID = GPSID;
    }

    public String getFKUserMasterId() {
        return FKUserMasterId;
    }

    public void setFKUserMasterId(String FKUserMasterId) {
        this.FKUserMasterId = FKUserMasterId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddedDT() {
        return AddedDT;
    }

    public void setAddedDT(String addedDT) {
        AddedDT = addedDT;
    }
}
