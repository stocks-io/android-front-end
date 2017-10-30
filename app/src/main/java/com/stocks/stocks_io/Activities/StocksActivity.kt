package com.stocks.stocks_io.Activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.stocks.stocks_io.Data.Endpoints
import com.stocks.stocks_io.Data.Endpoints.DEVBASEURL
import com.stocks.stocks_io.Model.PortfolioModel
import com.stocks.stocks_io.Model.UsersModel
import com.stocks.stocks_io.POJO.BaseMessage
import com.stocks.stocks_io.POJO.HistoryMessage
import com.stocks.stocks_io.POJO.OrderRequest
import com.stocks.stocks_io.R
import com.stocks.stocks_io.Utils.getUserToken
import kotlinx.android.synthetic.main.activity_stocks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class StocksActivity : AppCompatActivity() {

    val TAG = "StocksActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stocks)

        data_button.setOnClickListener { getUserHistory("1234567890") }
        logout_button.setOnClickListener { logout() }
        buy_button.setOnClickListener { buyStocks("TSLA", 5) }
        sell_button.setOnClickListener { sellStocks("TSLA", 5) }
    }

    private fun sellStocks(symbol: String, units: Int) {
        val token = getUserToken(applicationContext)
        Log.wtf(TAG, token)

        val retrofit = Retrofit.Builder()
                .baseUrl(DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val portfolioModel = retrofit.create(PortfolioModel::class.java)
        portfolioModel.sellStocks(token, units, symbol).enqueue(object : Callback<OrderRequest> {
            override fun onResponse(call: Call<OrderRequest>, response: Response<OrderRequest>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,
                            "Sold yo TSLA fam. u cray. Cash back: ${response.body()?.totalCost} - " +
                                    "Cash remaining: ${response.body()?.remainingCash}",
                            LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "KEEP YO TSLA FAM ${response.message()}", LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OrderRequest>, t: Throwable) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }

        })

    }

    private fun buyStocks(symbol: String, units: Int) {
        val token = getUserToken(applicationContext)

        val retrofit = Retrofit.Builder()
                .baseUrl(DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val portfolioModel = retrofit.create(PortfolioModel::class.java)
        portfolioModel.buyStocks(token, units, symbol).enqueue(object : Callback<OrderRequest> {
            override fun onResponse(call: Call<OrderRequest>, response: Response<OrderRequest>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,
                            "Gotchu some TSLA fam. Cost: ${response.body()?.totalCost} - " +
                                    "Cash remaining: ${response.body()?.remainingCash}",
                            LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "No TSLA for u fam ${response.message()}", LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<OrderRequest>, t: Throwable) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }

        })
    }

    private fun logout() {
        val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val token = preferences.getString(getString(R.string.token), "")

        val retrofit = Retrofit.Builder()
                .baseUrl(DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val usersModel = retrofit.create(UsersModel::class.java)
        usersModel.logoutUser(token).enqueue(object : Callback<BaseMessage> {
            override fun onResponse(call: Call<BaseMessage>, response: Response<BaseMessage>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "We logged out fam", LENGTH_LONG).show()
                    preferences.edit().clear().apply()
                } else {
                    Toast.makeText(applicationContext, "Failed to log out... Maybe we were never logged in in the first place???", LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<BaseMessage>, t: Throwable?) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }

        })
    }

    private fun getUserHistory(token: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Endpoints.DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val usersModel = retrofit.create(UsersModel::class.java)
        usersModel.getUserHistory(token).enqueue(object : Callback<List<HistoryMessage>> {
            override fun onFailure(call: Call<List<HistoryMessage>>, t: Throwable) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }

            override fun onResponse(call: Call<List<HistoryMessage>>, response: Response<List<HistoryMessage>>) {
                if (response.isSuccessful) {
                    for (historyMessage in response.body().orEmpty()) {
                        Log.wtf(TAG, "Date: ${historyMessage.Date} Value: ${historyMessage.Value}")
                    }
                } else {
                    Log.wtf(TAG, response.message().toString())
                }
            }
        })

    }
}
