package com.vritti.vwb.Beans;

import java.util.ArrayList;

public class EditDatasheetNew {

    String PKCssHeaderId,FKCustomerId,FKCssFormsId,FeedBackCallId,Responsebycustomer,PKCssDtlsID,PKCSSFormsQuesId,FKQuesId,Weightage,
            IsResponseMandatory,ExpectedResponse,IsBranching,IfResponseId,DisableQuesStr,GroupId,GroupName,MaxNoOfResponses,
            MaxExpectedResponse,ResponseType,QuesCode,QuesText,QM_ResponseType,ValueMin,ValueMax,SelectionText,SelectionValue,
            Notes,MaxValueText,QM_SelectionType,ControlWidth,IsExtMaster,ExtMasterName,IssuedTo,IsApprove,CSSFormsDesc;
    int SequenceNo,flag;

    String answer, answer_value;
    boolean isAns;
    ArrayList<String> filePathName;
    String AttachmentCount;


    public EditDatasheetNew(String pKCssFormsQuesID, String fKQuesId,
                            String expectedResponse, String selectionValue, int flag, String FormId) {
        this.FKQuesId = fKQuesId;
        this.PKCSSFormsQuesId = pKCssFormsQuesID;
        this.SelectionValue = selectionValue;
        this.ExpectedResponse = expectedResponse;
        this.flag = flag;
        this.FKCssFormsId = FormId;
    }

    public EditDatasheetNew() {

    }

    public String getPKCssHeaderId() {
        return PKCssHeaderId;
    }

    public void setPKCssHeaderId(String PKCssHeaderId) {
        this.PKCssHeaderId = PKCssHeaderId;
    }

    public String getFKCustomerId() {
        return FKCustomerId;
    }

    public void setFKCustomerId(String FKCustomerId) {
        this.FKCustomerId = FKCustomerId;
    }

    public String getFKCssFormsId() {
        return FKCssFormsId;
    }

    public void setFKCssFormsId(String FKCssFormsId) {
        this.FKCssFormsId = FKCssFormsId;
    }

    public String getFeedBackCallId() {
        return FeedBackCallId;
    }

    public void setFeedBackCallId(String feedBackCallId) {
        FeedBackCallId = feedBackCallId;
    }

    public String getResponsebycustomer() {
        return Responsebycustomer;
    }

    public void setResponsebycustomer(String responsebycustomer) {
        Responsebycustomer = responsebycustomer;
    }

    public String getPKCssDtlsID() {
        return PKCssDtlsID;
    }

    public void setPKCssDtlsID(String PKCssDtlsID) {
        this.PKCssDtlsID = PKCssDtlsID;
    }

    public String getPKCSSFormsQuesId() {
        return PKCSSFormsQuesId;
    }

    public void setPKCSSFormsQuesId(String PKCSSFormsQuesId) {
        this.PKCSSFormsQuesId = PKCSSFormsQuesId;
    }

    public String getFKQuesId() {
        return FKQuesId;
    }

    public void setFKQuesId(String FKQuesId) {
        this.FKQuesId = FKQuesId;
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

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
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

    public String getResponseType() {
        return ResponseType;
    }

    public void setResponseType(String responseType) {
        ResponseType = responseType;
    }

    public String getQuesCode() {
        return QuesCode;
    }

    public void setQuesCode(String quesCode) {
        QuesCode = quesCode;
    }

    public String getQuesText() {
        return QuesText;
    }

    public void setQuesText(String quesText) {
        QuesText = quesText;
    }

    public String getQM_ResponseType() {
        return QM_ResponseType;
    }

    public void setQM_ResponseType(String QM_ResponseType) {
        this.QM_ResponseType = QM_ResponseType;
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

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getMaxValueText() {
        return MaxValueText;
    }

    public void setMaxValueText(String maxValueText) {
        MaxValueText = maxValueText;
    }

    public String getQM_SelectionType() {
        return QM_SelectionType;
    }

    public void setQM_SelectionType(String QM_SelectionType) {
        this.QM_SelectionType = QM_SelectionType;
    }

    public String getControlWidth() {
        return ControlWidth;
    }

    public void setControlWidth(String controlWidth) {
        ControlWidth = controlWidth;
    }

    public String getIsExtMaster() {
        return IsExtMaster;
    }

    public void setIsExtMaster(String isExtMaster) {
        IsExtMaster = isExtMaster;
    }

    public String getExtMasterName() {
        return ExtMasterName;
    }

    public void setExtMasterName(String extMasterName) {
        ExtMasterName = extMasterName;
    }

    public String getIssuedTo() {
        return IssuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        IssuedTo = issuedTo;
    }

    public String getIsApprove() {
        return IsApprove;
    }

    public void setIsApprove(String isApprove) {
        IsApprove = isApprove;
    }

    public String getCSSFormsDesc() {
        return CSSFormsDesc;
    }

    public void setCSSFormsDesc(String CSSFormsDesc) {
        this.CSSFormsDesc = CSSFormsDesc;
    }

    /**********/

    public String getAnswer() {
        return answer;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isAns() {
        return isAns;
    }

    public void setAns(boolean ans) {
        isAns = ans;
    }


    public ArrayList<String> getFilePathName() {
        return filePathName;
    }


    public void setFilePathName(ArrayList<String> filePathName) {
        this.filePathName = filePathName;
    }

    public String getAttachmentCount() {
        return AttachmentCount;
    }

    public void setAttachmentCount(String attachmentCount) {
        AttachmentCount = attachmentCount;
    }
}
