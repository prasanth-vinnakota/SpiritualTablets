package com.example.spiritualtablets.models;

import com.google.firebase.database.ServerValue;

public class MessageItem {

    private String message;
    private String name;
    private String userId;
    private Object timeStamp;

    public MessageItem(){}

    public MessageItem(String message, String name, String userId) {
        this.message = message;
        this.name = name;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.userId = userId;
    }


    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public String getUserId(){ return userId;}

}
