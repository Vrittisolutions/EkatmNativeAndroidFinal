package com.vritti.crm.bean;

public class DatasheetList {

String PKCssFormsId;
    String UseForProspect;
    String Result;
    String Mode;
    String PKSuspectId;
    String CSSFormsCode;
    String CSSFormsDesc;
    String PKCssHeaderId;

    public String getPKSuspectId() {
        return PKSuspectId;
    }

    public void setPKSuspectId(String PKSuspectId) {
        this.PKSuspectId = PKSuspectId;
    }

    public String getPKCssFormsId() {
        return PKCssFormsId;
    }

    public void setPKCssFormsId(String PKCssFormsId) {
        this.PKCssFormsId = PKCssFormsId;
    }

    public String getPKCssHeaderId() {
        return PKCssHeaderId;
    }

    public void setPKCssHeaderId(String PKCssHeaderId) {
        this.PKCssHeaderId = PKCssHeaderId;
    }

    public String getUseForProspect() {
        return UseForProspect;

    }

    public void setUseForProspect(String useForProspect) {
        UseForProspect = useForProspect;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getCSSFormsCode() {
        return CSSFormsCode;
    }

    public void setCSSFormsCode(String CSSFormsCode) {
        this.CSSFormsCode = CSSFormsCode;
    }

    public String getCSSFormsDesc() {
        return CSSFormsDesc;
    }

    public void setCSSFormsDesc(String CSSFormsDesc) {
        this.CSSFormsDesc = CSSFormsDesc;
    }


}
