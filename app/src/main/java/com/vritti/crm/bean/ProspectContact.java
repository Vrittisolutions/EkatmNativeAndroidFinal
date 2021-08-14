package com.vritti.crm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharvari on 09-Feb-17.
 */

public class ProspectContact {

    private String name;
    private String Designation;
    private String Department;
    private String birthdate;
    private String EmailId;
    private String Telephone;
    private String Mobile;
    private String Fax;
    private String whtsapp;
    String id;
    ArrayList<ProspectContact> prospectContactArrayList;


    public ArrayList<ProspectContact> getProspectContactArrayList() {
        return prospectContactArrayList;
    }

    public void setProspectContactArrayList(ArrayList<ProspectContact> prospectContactArrayList) {
        this.prospectContactArrayList = prospectContactArrayList;
    }

    public ProspectContact(String name, String Designation, String Department, String birthdate,
                           String EmailId, String Telephone, String Mobile, String Fax, String whtsapp)
    {
        this.name=name;
        this.Designation=Designation;
        this.Department=Department;
        this.birthdate=birthdate;
        this.EmailId=EmailId;
        this.Telephone=Telephone;
        this.Mobile=Mobile;
        this.Fax=Fax;
        this.whtsapp=whtsapp;
    }


    public ProspectContact(ArrayList<ProspectContact> lstContact) {
        this.prospectContactArrayList = lstContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getWhtsapp() {
        return whtsapp;
    }

    public void setWhtsapp(String whtsapp) {
        this.whtsapp = whtsapp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}