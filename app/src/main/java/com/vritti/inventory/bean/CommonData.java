package com.vritti.inventory.bean;

import java.io.Serializable;

/**
 * Created by sharvari on 16-Apr-18.
 */

public class CommonData implements Serializable {


    String path,Filename,Latitude,Longitude,id,description;

    public CommonData() {

    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommonData(String path, String attachFilename, String latitude, String longitude) {
        this.path = path;
        Filename = attachFilename;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String attachFilename) {
        Filename = attachFilename;
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
}
