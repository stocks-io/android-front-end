package com.stocks.stocks_io.POJO;

public class BaseMessage {
    private String message;

    public BaseMessage(String message) {
        this.message = message;
    }


    public BaseMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
