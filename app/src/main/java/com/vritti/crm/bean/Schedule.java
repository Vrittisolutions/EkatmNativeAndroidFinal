package com.vritti.crm.bean;

import java.io.Serializable;

public class Schedule implements Serializable {

    String  TheDate,DOW,Visit,Telephone,Email,VisitPlan,TravelPlan;

    public String getTheDate() {
        return TheDate;
    }

    public void setTheDate(String theDate) {
        TheDate = theDate;
    }

    public String getDOW() {
        return DOW;
    }

    public void setDOW(String DOW) {
        this.DOW = DOW;
    }

    public String getVisit() {
        return Visit;
    }

    public void setVisit(String visit) {
        Visit = visit;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getVisitPlan() {
        return VisitPlan;
    }

    public void setVisitPlan(String visitPlan) {
        VisitPlan = visitPlan;
    }

    public String getTravelPlan() {
        return TravelPlan;
    }

    public void setTravelPlan(String travelPlan) {
        TravelPlan = travelPlan;
    }
}
