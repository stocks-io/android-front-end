package com.stocks.stocks_io.Model

import com.stocks.stocks_io.POJO.BaseMessage
import com.stocks.stocks_io.POJO.HistoryMessage
import com.stocks.stocks_io.POJO.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UsersModel {

    @POST("users/register")
    @FormUrlEncoded
    fun registerUser(@Field("firstName") firstName: String,
                     @Field("lastName") lastName: String,
                     @Field("email") email: String,
                     @Field("password") password: String): Call<BaseMessage>

    @POST("users/login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email: String,
                  @Field("password") password: String): Call<LoginResponse>

    @POST("users/history")
    @FormUrlEncoded
    fun getUserHistory(@Field("token") token: String): Call<List<HistoryMessage>>

    @POST("users/logout")
    @FormUrlEncoded
    fun logoutUser(@Field("token") token: String): Call<BaseMessage>
}
