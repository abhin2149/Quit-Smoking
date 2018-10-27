package com.example.abhinav.quitsmoking;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private long messageTime;
    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    private String messageUser;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;

        this.messageUser=messageUser;


        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }



    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

