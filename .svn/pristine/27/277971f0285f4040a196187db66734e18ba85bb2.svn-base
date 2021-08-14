package com.vritti.chat.bean;

public class ChatModelObject extends ListObject{
    ChatMessage chatMessage;

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public int getType() {
        String typeView = this.chatMessage.getStatus();
        if (typeView.equals("Sender")) {
            return TYPE_GENERAL_RIGHT;
        } else {
            return TYPE_GENERAL_LEFT;
        }


    }
}
