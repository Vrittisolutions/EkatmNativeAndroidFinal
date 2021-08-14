package com.vritti.vwb.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 300151 on 10/17/2016.
 */
public class MyTeamBean implements Serializable {
    String UserName;
    String UserMasterId;
    String CountOnTime;
    String TotalCount;
    String OnTimePerc;
    String TotalOverdueActivities,DayDiff;
    String TotalCritical,TotalUnapproved,TotalAssigned,Longitude,Latitude,LocationName,ImagePath,Gender,Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTotalCritical() {
        return TotalCritical;
    }

    public void setTotalCritical(String totalCritical) {
        TotalCritical = totalCritical;
    }

    public String getTotalUnapproved() {
        return TotalUnapproved;
    }

    public void setTotalUnapproved(String totalUnapproved) {
        TotalUnapproved = totalUnapproved;
    }

    public String getTotalAssigned() {
        return TotalAssigned;
    }

    public void setTotalAssigned(String totalAssigned) {
        TotalAssigned = totalAssigned;
    }

    public String getUserName() {
        return UserName;
    }

    public String getTotalOverdueActivities() {
        return TotalOverdueActivities;
    }

    public void setTotalOverdueActivities(String totalOverdueActivities) {
        TotalOverdueActivities = totalOverdueActivities;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getCountOnTime() {
        return CountOnTime;
    }

    public void setCountOnTime(String countOnTime) {
        CountOnTime = countOnTime;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getOnTimePerc() {
        return OnTimePerc;
    }

    public void setOnTimePerc(String onTimePerc) {
        OnTimePerc = onTimePerc;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDayDiff() {
        return DayDiff;
    }

    public void setDayDiff(String dayDiff) {
        DayDiff = dayDiff;
    }


}
