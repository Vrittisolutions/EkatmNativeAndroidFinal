package com.vritti.sales.beans;

import android.widget.CheckBox;

public class ProspectSetting {

    String Caption;
    boolean isvisiblecheck;
    boolean ismandatorycheck;
    String FKProspectHdrID, PKFieldID, ProspectField,Section, FieldType,PKUserId;
    boolean isSelected;

    public ProspectSetting(String name, boolean b, boolean b1) {
    }

    public ProspectSetting() {

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String name) {
        this.Caption = name;
    }

    public boolean isIsvisiblecheck() {
        return isvisiblecheck;
    }

    public void setIsvisiblecheck(boolean isvisiblecheck) {
        this.isvisiblecheck = isvisiblecheck;
    }

    public boolean isIsmandatorycheck() {
        return ismandatorycheck;
    }

    public void setIsmandatorycheck(boolean ismandatorycheck) {
        this.ismandatorycheck = ismandatorycheck;
    }

    public String getFKProspectHdrID() {
        return FKProspectHdrID;
    }

    public void setFKProspectHdrID(String FKProspectHdrID) {
        this.FKProspectHdrID = FKProspectHdrID;
    }

    public String getPKFieldID() {
        return PKFieldID;
    }

    public void setPKFieldID(String PKFieldID) {
        this.PKFieldID = PKFieldID;
    }

    public String getProspectField() {
        return ProspectField;
    }

    public void setProspectField(String prospectField) {
        ProspectField = prospectField;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getFieldType() {
        return FieldType;
    }

    public void setFieldType(String fieldType) {
        FieldType = fieldType;
    }

    public String getPKUserId() {
        return PKUserId;
    }

    public void setPKUserId(String PKUserId) {
        this.PKUserId = PKUserId;
    }
}
