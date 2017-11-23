package com.stocks.stocks_io.Model

import com.stocks.stocks_io.POJO.Options
import com.stocks.stocks_io.POJO.OrderRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PortfolioModel {
    @POST("/portfolio/buy")
    @FormUrlEncoded
    fun buyStocks(@Field("token") token: String,
                  @Field("units") units: Int,
                  @Field("symbol") symbol: String): Call<OrderRequest>

    @POST("/portfolio/sell")
    @FormUrlEncoded
    fun sellStocks(@Field("token") token: String,
                   @Field("units") units: Int,
                   @Field("symbol") symbol: String): Call<OrderRequest>


    @POST("/portfolio/owned")
    @FormUrlEncoded
    fun getUserStocks(@Field("token") token: String): Call<List<Options>>

    @POST("/portfolio/owned")
    @FormUrlEncoded
    fun getUserStocksDebug(@Field("token") token: String): Call<ResponseBody>
}