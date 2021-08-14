package com.vritti.inventory.physicalInventory.bean;

import java.io.Serializable;

public class LocationList implements Serializable {


    String LocationMasterId,LocationCode,LocationDesc;

    public String getLocationMasterId() {
        return LocationMasterId;
    }

    public void setLocationMasterId(String locationMasterId) {
        LocationMasterId = locationMasterId;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getLocationDesc() {
        return LocationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        LocationDesc = locationDesc;
    }
}
