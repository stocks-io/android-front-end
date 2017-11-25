package com.stocks.stocks_io.Model

import com.stocks.stocks_io.POJO.Options
import com.stocks.stocks_io.POJO.OrderRequest
import com.stocks.stocks_io.POJO.StockHistory
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/portfolio/stockhistory/")
    fun getStockHistory(@Query("token") token: String,
                        @Query("symbol") symbol: String,
                        @Query("timeframe") timeframe: String): Call<StockHistory>
}