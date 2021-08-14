package com.vritti.vwb.Beans;

/**
 * Created by 300151 on 11/24/2016.
 */
public class ClaimDetailsBean {
    String VehicleType,Distance,ClaimDate,Amount,fromPlace,ToPlace,tv_mode,tv_travelling,tv_lodging,tv_food,tv_Local,tv_Ph,tv_Maintenanace;

    public String getClaimDate() {
        return ClaimDate;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public void setClaimDate(String claimDate) {
        ClaimDate = claimDate;
    }

    public String getAmount() {
        return Amount;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return ToPlace;
    }

    public void setToPlace(String toPlace) {
        ToPlace = toPlace;
    }

    public String getTv_mode() {
        return tv_mode;
    }

    public void setTv_mode(String tv_mode) {
        this.tv_mode = tv_mode;
    }

    public String getTv_travelling() {
        return tv_travelling;
    }

    public void setTv_travelling(String tv_travelling) {
        this.tv_travelling = tv_travelling;
    }

    public String getTv_lodging() {
        return tv_lodging;
    }

    public void setTv_lodging(String tv_lodging) {
        this.tv_lodging = tv_lodging;
    }

    public String getTv_food() {
        return tv_food;
    }

    public void setTv_food(String tv_food) {
        this.tv_food = tv_food;
    }

    public String getTv_Local() {
        return tv_Local;
    }

    public void setTv_Local(String tv_Local) {
        this.tv_Local = tv_Local;
    }

    public String getTv_Ph() {
        return tv_Ph;
    }

    public void setTv_Ph(String tv_Ph) {
        this.tv_Ph = tv_Ph;
    }

    public String getTv_Maintenanace() {
        return tv_Maintenanace;
    }

    public void setTv_Maintenanace(String tv_Maintenanace) {
        this.tv_Maintenanace = tv_Maintenanace;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
