package com.example.spiritualtablets.models;

import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;

import java.io.Serializable;

public class Message implements Serializable {
    private String id, message, url, title, description;
    private Type type;
    public Message() {
        this.type = Type.TEXT;
    }


    public Message(RuntimeResponseGeneric r) {
        this.message = "";
        this.title = r.title();
        this.description = r.description();
        this.url = r.source();
        this.id = "2";
        this.type = Type.IMAGE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        TEXT,
        IMAGE,
        OPTIONS
    }
}
