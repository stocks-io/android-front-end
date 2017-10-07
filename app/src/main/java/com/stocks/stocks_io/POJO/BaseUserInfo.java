package com.stocks.stocks_io.POJO;

public class BaseUserInfo {
    private String username;
    private String password;

    public BaseUserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public BaseUserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
