package com.stocks.stocks_io.POJO;

public class LoginResponse {
    private String userId;
    private String token;
    private String expires;

    public LoginResponse() {}

    public LoginResponse(String userId, String token, String expires) {
        this.userId = userId;
        this.token = token;
        this.expires = expires;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
