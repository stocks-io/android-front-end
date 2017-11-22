package com.stocks.stocks_io.Model

import com.stocks.stocks_io.POJO.StockPrice
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StockPriceOptions {

    @GET("stock/{symbol}/quote")
    fun getStockPrice(@Path("symbol") symbol: String): Call<StockPrice>
}
