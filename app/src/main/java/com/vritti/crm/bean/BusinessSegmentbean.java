package com.vritti.crm.bean;

/**
 * Created by sharvari on 14-Feb-17.
 */

public class BusinessSegmentbean {
    String
    PKBusiSegmentID ,
            SegmentCode ,
            SegmentDescription ,
            FKParentBusiSegmentID;

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

    public String getFKParentBusiSegmentID() {
        return FKParentBusiSegmentID;
    }

    public void setFKParentBusiSegmentID(String FKParentBusiSegmentID) {
        this.FKParentBusiSegmentID = FKParentBusiSegmentID;
    }
    @Override
    public String toString() {
        // return super.toString();
        return this.SegmentDescription;
    }
}