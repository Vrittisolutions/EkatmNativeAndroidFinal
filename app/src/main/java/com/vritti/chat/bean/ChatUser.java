package com.vritti.chat.bean;

import java.io.Serializable;

/**
 * Created by pradnya on 31-Oct-17.
 */

public class ChatUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ChatType,Count,Username,UserMasterId,Mobile,Chatroom,Creater,ChatRoomId,Status,StartTime,ParticipantId,ChatSourceId,AddedBy,ParticipantName,Message , ImagePath;
    private boolean selected;

    public ChatUser() {
        // TODO Auto-generated constructor stub
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }


    public String getChatType() {
        return ChatType;
    }

    public void setChatType(String chatType) {
        ChatType = chatType;
    }

    public ChatUser(String userMasterId, String username, String Mobile) {
        Username = username;
        UserMasterId = userMasterId;
        this.Mobile=Mobile;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getParticipantId() {
        return ParticipantId;
    }

    public void setParticipantId(String participantId) {
        ParticipantId = participantId;
    }

    public String getChatSourceId() {
        return ChatSourceId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setChatSourceId(String chatSourceId) {
        ChatSourceId = chatSourceId;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getParticipantName() {
        return ParticipantName;
    }

    public void setParticipantName(String participantName) {
        ParticipantName = participantName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getChatroom() {
        return Chatroom;
    }

    public void setChatroom(String chatroom) {
        Chatroom = chatroom;
    }

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }
}
