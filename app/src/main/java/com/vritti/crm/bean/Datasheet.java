package com.vritti.crm.bean;

public class Datasheet {
    int id, SequenceNo;
    String FKQuesId, Weightage, QuesText,
            IsResponseMandatory, SelectionText, SelectionValue, ValueMin,
            ValueMax, Notes, MaxValueText, FKPrimaryQuesId, FKSecondaryQuesId,
            IsBranching, ExpectedResponse, IfResponseId, DisableQuesStr,
            GroupID, GroupName, QuesCode, SelectionType, ControlWidth,
            MaxNoOfResponses, MaxExpectedResponse, ResponseType, ResponseValue,
            answer, answer_value, detailid, FormId,PKCssFormsQuesID;

    int flag;

    public Datasheet(String pKCssFormsQuesID, String fKQuesId,
                     String expectedResponse, String selectionValue, int flag, String FormId) {
        super();
        this.FKQuesId = fKQuesId;
        this.PKCssFormsQuesID = pKCssFormsQuesID;
        this.SelectionValue = selectionValue;
        this.ExpectedResponse = expectedResponse;
        this.flag = flag;
        this.FormId = FormId;
    }

    public Datasheet(int sequenceNo, String FKQuesId, String weightage, String quesText,
                     String isResponseMandatory, String selectionText, String selectionValue,
                     String valueMin, String valueMax, String notes, String maxValueText,
                     String FKPrimaryQuesId, String FKSecondaryQuesId, String isBranching,
                     String expectedResponse, String ifResponseId, String disableQuesStr, String groupID,
                     String groupName, String quesCode, String selectionType, String controlWidth,
                     String maxNoOfResponses, String maxExpectedResponse, String responseType,
                     String responseValue, String answer, String answer_value, String detailid,
                     String formId, String PKCssFormsQuesID, int flag) {
        SequenceNo = sequenceNo;
        this.FKQuesId = FKQuesId;
        Weightage = weightage;
        QuesText = quesText;
        IsResponseMandatory = isResponseMandatory;
        SelectionText = selectionText;
        SelectionValue = selectionValue;
        ValueMin = valueMin;
        ValueMax = valueMax;
        Notes = notes;
        MaxValueText = maxValueText;
        this.FKPrimaryQuesId = FKPrimaryQuesId;
        this.FKSecondaryQuesId = FKSecondaryQuesId;
        IsBranching = isBranching;
        ExpectedResponse = expectedResponse;
        IfResponseId = ifResponseId;
        DisableQuesStr = disableQuesStr;
        GroupID = groupID;
        GroupName = groupName;
        QuesCode = quesCode;
        SelectionType = selectionType;
        ControlWidth = controlWidth;
        MaxNoOfResponses = maxNoOfResponses;
        MaxExpectedResponse = maxExpectedResponse;
        ResponseType = responseType;
        ResponseValue = responseValue;
        this.answer = answer;
        this.answer_value = answer_value;
        this.detailid = detailid;
        FormId = formId;
        this.PKCssFormsQuesID = PKCssFormsQuesID;
        this.flag = flag;
    }

    public String getFormId() {
        return FormId;
    }

    public void setFormId(String formId) {
        FormId = formId;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public Datasheet() {
        // TODO Auto-generated constructor stub
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getSequenceNo() {
        return SequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        SequenceNo = sequenceNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getQuesText() {
        return QuesText;
    }

    public void setQuesText(String quesText) {
        QuesText = quesText;
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

    public String getIsBranching() {
        return IsBranching;
    }

    public void setIsBranching(String isBranching) {
        IsBranching = isBranching;
    }

    public String getExpectedResponse() {
        return ExpectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        ExpectedResponse = expectedResponse;
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
}
