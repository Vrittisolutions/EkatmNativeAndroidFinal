package com.vritti.vwb.ImageWithLocation;

class LocationTypePojoClass {

    String locationTypeID  , LocationType;

    public LocationTypePojoClass(String locationTypeID, String locationType) {
        this.locationTypeID = locationTypeID;
        LocationType = locationType;
    }

    public String getLocationTypeID() {
        return locationTypeID;
    }

    public void setLocationTypeID(String locationTypeID) {
        this.locationTypeID = locationTypeID;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }

    @Override
    public String toString() {
        return LocationType;
    }
}
