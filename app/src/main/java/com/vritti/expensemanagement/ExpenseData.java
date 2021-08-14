package com.vritti.expensemanagement;

import java.io.Serializable;

/**
 * Created by sharvari on 19-Sep-19.
 */

public class ExpenseData implements Serializable {

   /* String cat_name,sub_name,cash_amt,wallet_type,card_no,exp_type,exp_amount
    ,payment_mode,mode_travel,Source,Destination,datetim,attachment,Remark,KM,UserMasterId;
    int ExpRecordId;*/


    String UserMasterId,cat_name,ExpType,Amount,PaymentMode,TravelMode,
            ExpDate,attachment,Remark,Distance,FromLocation,ToLocation,VehicleType,LinkTo,LinkId,Path;
    String ExpRecordId;
    double tot_prev_exp=0;

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getExpRecordId() {
        return ExpRecordId;
    }

    public void setExpRecordId(String expRecordId) {
        ExpRecordId = expRecordId;
    }

    public String getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getExpType() {
        return ExpType;
    }

    public void setExpType(String expType) {
        ExpType = expType;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getTravelMode() {
        return TravelMode;
    }

    public void setTravelMode(String travelMode) {
        TravelMode = travelMode;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getFromLocation() {
        return FromLocation;
    }

    public void setFromLocation(String fromLocation) {
        FromLocation = fromLocation;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public void setToLocation(String toLocation) {
        ToLocation = toLocation;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getLinkTo() {
        return LinkTo;
    }

    public void setLinkTo(String linkTo) {
        LinkTo = linkTo;
    }

    public String getLinkId() {
        return LinkId;
    }

    public void setLinkId(String linkId) {
        LinkId = linkId;
    }

    public double getTot_prev_exp() {
        return tot_prev_exp;
    }

    public void setTot_prev_exp(double tot_prev_exp) {
        this.tot_prev_exp = tot_prev_exp;
    }
}
