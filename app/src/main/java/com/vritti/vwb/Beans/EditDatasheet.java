package com.vritti.vwb.Beans;

import java.util.ArrayList;

public class EditDatasheet {
    String attachfilename;
    String path;
    String ExpectedResponse;
    String QuesText;
    String FKQuesId;
    String PKCssFormsQuesID;
    String Weightage;
    String IsResponseMandatory;
    String SelectionText;
    String SelectionValue;
    String ValueMin;
    String ValueMax;
    String MaxValueText,descr,username,IsApprDisAppr,remark,Addeddt;
    boolean isAns;
    boolean isRemarks;
    String ResponseType;
    String ResponseValue;
    String SelectionType;
    String ControlWidth;
    String MaxNoOfResponses;
    String MaxExpectedResponse;
    String pkcssdtlsid;
    String FKCSSheaderid;
    String responsebycustomer;
    String selectionvalue1;
    String activityid;
    String detailid;
    String FormId;
    String apprStatus;
    String AttachmentCount;
    String IsSingleAttachment;
    String isAttachment;
    String remarks;
    String note;
    String Notes;
    String FKPrimaryQuesId;
    String FKSecondaryQuesId;
    String IfResponseId1;
    String IsBranching;
    String IfResponseId;
    String DisableQuesStr;
    String GroupID;
    String GroupName;
    String QuesCode;
    String answer, answer_value;
    int flag,SequenceNo,isApproved = 0;
    boolean isSelected;
    ArrayList<String> filePathName;

    public String getAttachmentCount() { return AttachmentCount; }

    public void setAttachmentCount(String attachmentCount) { AttachmentCount = attachmentCount; }

    public String getApprStatus() {
        return apprStatus;
    }

    public void setApprStatus(String apprStatus) {
        this.apprStatus = apprStatus;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getAnswer() {
        return answer;
    }

    public String getIsAttachment() {
        return isAttachment;
    }

    public void setIsAttachment(String isAttachment) {
        this.isAttachment = isAttachment;
    }


    public String getDetailid() {
        return detailid;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getFormId() {
        return FormId;
    }

    public void setFormId(String formId) {
        FormId = formId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer_value() {
        return answer_value;
    }

    public void setAnswer_value(String answer_value) {
        this.answer_value = answer_value;
    }

    public String getActivityid() {
        return activityid;
    }

    public void setActivityid(String activityid) {
        this.activityid = activityid;
    }

    public String getAttachfilename() {
        return attachfilename;
    }

    public void setAttachfilename(String attachfilename) {
        this.attachfilename = attachfilename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSequenceNo() {
        return SequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        SequenceNo = sequenceNo;
    }

    public String getExpectedResponse() {
        return ExpectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        ExpectedResponse = expectedResponse;
    }

    public String getQuesText() {
        return QuesText;
    }

    public void setQuesText(String quesText) {
        QuesText = quesText;
    }

    public String getFKQuesId() {
        return FKQuesId;
    }

    public void setFKQuesId(String fKQuesId) {
        FKQuesId = fKQuesId;
    }

    public String getPKCssFormsQuesID() {
        return PKCssFormsQuesID;
    }

    public void setPKCssFormsQuesID(String pKCssFormsQuesID) {
        PKCssFormsQuesID = pKCssFormsQuesID;
    }

    public String getWeightage() {
        return Weightage;
    }

    public void setWeightage(String weightage) {
        Weightage = weightage;
    }

    public String getIsResponseMandatory() {
        return IsResponseMandatory;
    }

    public void setIsResponseMandatory(String isResponseMandatory) {
        IsResponseMandatory = isResponseMandatory;
    }

    public String getSelectionText() {
        return SelectionText;
    }

    public void setSelectionText(String selectionText) {
        SelectionText = selectionText;
    }

    public String getSelectionValue() {
        return SelectionValue;
    }

    public void setSelectionValue(String selectionValue) {
        SelectionValue = selectionValue;
    }

    public String getValueMin() {
        return ValueMin;
    }

    public void setValueMin(String valueMin) {
        ValueMin = valueMin;
    }

    public String getValueMax() {
        return ValueMax;
    }

    public void setValueMax(String valueMax) {
        ValueMax = valueMax;
    }

    public String getMaxValueText() {
        return MaxValueText;
    }

    public void setMaxValueText(String maxValueText) {
        MaxValueText = maxValueText;
    }

    public String getResponseType() {
        return ResponseType;
    }

    public void setResponseType(String responseType) {
        ResponseType = responseType;
    }

    public String getResponseValue() {
        return ResponseValue;
    }

    public void setResponseValue(String responseValue) {
        ResponseValue = responseValue;
    }

    public String getSelectionType() {
        return SelectionType;
    }

    public void setSelectionType(String selectionType) {
        SelectionType = selectionType;
    }

    public String getControlWidth() {
        return ControlWidth;
    }

    public void setControlWidth(String controlWidth) {
        ControlWidth = controlWidth;
    }

    public String getMaxNoOfResponses() {
        return MaxNoOfResponses;
    }

    public void setMaxNoOfResponses(String maxNoOfResponses) {
        MaxNoOfResponses = maxNoOfResponses;
    }

    public String getMaxExpectedResponse() {
        return MaxExpectedResponse;
    }

    public void setMaxExpectedResponse(String maxExpectedResponse) {
        MaxExpectedResponse = maxExpectedResponse;
    }

    public String getPkcssdtlsid() {
        return pkcssdtlsid;
    }

    public void setPkcssdtlsid(String pkcssdtlsid) {
        this.pkcssdtlsid = pkcssdtlsid;
    }

    public String getFKCSSheaderid() {
        return FKCSSheaderid;
    }

    public void setFKCSSheaderid(String fKCSSheaderid) {
        FKCSSheaderid = fKCSSheaderid;
    }

    public String getResponsebycustomer() {
        return responsebycustomer;
    }

    public void setResponsebycustomer(String responsebycustomer) {
        this.responsebycustomer = responsebycustomer;
    }

    public String getSelectionvalue1() {
        return selectionvalue1;
    }

    public void setSelectionvalue1(String selectionvalue1) {
        this.selectionvalue1 = selectionvalue1;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getFKPrimaryQuesId() {
        return FKPrimaryQuesId;
    }

    public void setFKPrimaryQuesId(String fKPrimaryQuesId) {
        FKPrimaryQuesId = fKPrimaryQuesId;
    }

    public String getFKSecondaryQuesId() {
        return FKSecondaryQuesId;
    }

    public void setFKSecondaryQuesId(String fKSecondaryQuesId) {
        FKSecondaryQuesId = fKSecondaryQuesId;
    }

    public String getIfResponseId1() {
        return IfResponseId1;
    }

    public void setIfResponseId1(String ifResponseId1) {
        IfResponseId1 = ifResponseId1;
    }

    public String getIsBranching() {
        return IsBranching;
    }

    public void setIsBranching(String isBranching) {
        IsBranching = isBranching;
    }

    public String getIfResponseId() {
        return IfResponseId;
    }

    public void setIfResponseId(String ifResponseId) {
        IfResponseId = ifResponseId;
    }

    public String getDisableQuesStr() {
        return DisableQuesStr;
    }

    public void setDisableQuesStr(String disableQuesStr) {
        DisableQuesStr = disableQuesStr;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getQuesCode() {
        return QuesCode;
    }

    public void setQuesCode(String quesCode) {
        QuesCode = quesCode;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsApprDisAppr() {
        return IsApprDisAppr;
    }

    public void setIsApprDisAppr(String isApprDisAppr) {
        IsApprDisAppr = isApprDisAppr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddeddt() {
        return Addeddt;
    }

    public void setAddeddt(String addeddt) {
        Addeddt = addeddt;
    }

    public boolean isAns() {
        return isAns;
    }

    public void setAns(boolean ans) {
        isAns = ans;
    }

    public boolean isRemarks() {
        return isRemarks;
    }

    public void setRemarks(boolean remarks) {
        isRemarks = remarks;
    }

    public String getIsSingleAttachment() {
        return IsSingleAttachment;
    }

    public void setIsSingleAttachment(String isSingleAttachment) {
        IsSingleAttachment = isSingleAttachment;
    }

    public ArrayList<String> getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(ArrayList<String> filePathName) {
        this.filePathName = filePathName;
    }
}
