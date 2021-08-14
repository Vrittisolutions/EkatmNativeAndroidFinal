package com.vritti.sales.OrderBookingNew.Bean;

import java.io.Serializable;

public class Business implements Serializable {

    String PKBusiSegmentID,SegmentCode,SegmentDescription, segmntImgPath,MerchAlisName = "";

    public String getPKBusiSegmentID() {
        return PKBusiSegmentID;
    }

    public void setPKBusiSegmentID(String PKBusiSegmentID) {
        this.PKBusiSegmentID = PKBusiSegmentID;
    }

    public String getSegmentCode() {
        return SegmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        SegmentCode = segmentCode;
    }

    public String getSegmentDescription() {
        return SegmentDescription;
    }

    public void setSegmentDescription(String segmentDescription) {
        SegmentDescription = segmentDescription;
    }

    public String getSegmntImgPath() {
        return segmntImgPath;
    }

    public void setSegmntImgPath(String segmntImgPath) {
        this.segmntImgPath = segmntImgPath;
    }

    public String getMerchAlisName() {
        return MerchAlisName;
    }

    public void setMerchAlisName(String merchAlisName) {
        MerchAlisName = merchAlisName;
    }
}
