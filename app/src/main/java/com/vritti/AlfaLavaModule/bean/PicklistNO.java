package com.vritti.AlfaLavaModule.bean;

import java.io.Serializable;

public class PicklistNO implements Serializable {

    String Pick_listHdrId,PicklistNo,AWBNO,Remark,QCStatus;

    public String getQCStatus() {
        return QCStatus;
    }

    public void setQCStatus(String QCStatus) {
        this.QCStatus = QCStatus;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getAWBNO() {
        return AWBNO;
    }

    public void setAWBNO(String AWBNO) {
        this.AWBNO = AWBNO;
    }

    public String getPick_listHdrId() {
        return Pick_listHdrId;
    }

    public void setPick_listHdrId(String pick_listHdrId) {
        Pick_listHdrId = pick_listHdrId;
    }

    public String getPicklistNo() {
        return PicklistNo;
    }

    public void setPicklistNo(String picklistNo) {
        PicklistNo = picklistNo;
    }
}
