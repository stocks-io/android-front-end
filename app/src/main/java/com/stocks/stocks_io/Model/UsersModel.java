package com.stocks.stocks_io.Model;

import com.stocks.stocks_io.POJO.BaseMessage;
import com.stocks.stocks_io.POJO.BaseUserInfo;
import com.stocks.stocks_io.POJO.FullUserInfo;
import com.stocks.stocks_io.POJO.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersModel {
    @POST("users/register")
    Call<BaseMessage> registerUser(@Body FullUserInfo userInfo);

    @POST("users/login")
    Call<LoginResponse> loginUser(@Body BaseUserInfo userInfo);
}
