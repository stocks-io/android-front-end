package com.stocks.stocks_io.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.stocks.stocks_io.Adapters.OptionsAdapter
import com.stocks.stocks_io.Adapters.StockClickListener
import com.stocks.stocks_io.Data.Endpoints
import com.stocks.stocks_io.Data.Endpoints.DEVBASEURL
import com.stocks.stocks_io.Data.Endpoints.STOCKS_BASE_URL
import com.stocks.stocks_io.Fragments.PopupBuySellFragment
import com.stocks.stocks_io.Fragments.PopupSearchFragment
import com.stocks.stocks_io.Model.PortfolioModel
import com.stocks.stocks_io.Model.StockPriceOptions
import com.stocks.stocks_io.Model.UsersModel
import com.stocks.stocks_io.POJO.BaseMessage
import com.stocks.stocks_io.POJO.ExtendedOptions
import com.stocks.stocks_io.POJO.HistoryMessage
import com.stocks.stocks_io.R
import kotlinx.android.synthetic.main.activity_stocks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class StocksActivity : AppCompatActivity() {

    val TAG = "StocksActivity"
    val MOCK_TOKEN = "E48D1744-6042-4F8B-BC67-3E62B1AC77ED"
    var num = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stocks)

        buy_fab?.setOnClickListener {
            val searchFragment = PopupSearchFragment()
            searchFragment.show(fragmentManager, "RETURN OF THE JEBI")
        }

        getUserHistory("1234567890")
        getStockWatch()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.leaderboard -> startActivity(Intent(this, LeaderboardActivity::class.java))
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stocks, menu)
        return true
    }

    private fun getStockWatch() {
        Thread {
            val usersAsync = Retrofit.Builder()
                    .baseUrl(DEVBASEURL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

            val stocksAsync = Retrofit.Builder()
                    .baseUrl(STOCKS_BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

            val orders = usersAsync.create(PortfolioModel::class.java)

            val stocks = stocksAsync.create(StockPriceOptions::class.java)

            orders.getUserStocks(MOCK_TOKEN).execute().body()?.let {

                val extendedOptions = it.map { ExtendedOptions(it.Symbol, it.Units, stocks.getStockPrice(it.Symbol).execute().body()?.latestPrice ?: -1.0) }
                Handler(Looper.getMainLooper()).post({
                    options.layoutManager = LinearLayoutManager(applicationContext)
                    val optionsAdapter = OptionsAdapter(extendedOptions)
                    optionsAdapter.stockClickLister = object : StockClickListener {
                        override fun onStockClicked(option: ExtendedOptions) {
                            val stockFragment = PopupBuySellFragment()
                            stockFragment.options = option
                            stockFragment.dismissListener = {
                                getStockWatch()
                            }
                            stockFragment.show(fragmentManager, "PIZZA DOG")
                        }
                    }
                    options.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            if (dy > 0) {
                                // Scroll Down
                                if (buy_fab.isShown) {
                                    buy_fab.hide()
                                }
                            } else if (dy < 0) {
                                // Scroll Up
                                if (!buy_fab.isShown) {
                                    buy_fab.show()
                                }
                            }

                        }
                    })
                    options.adapter = optionsAdapter
                })
            }
        }.start()
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

        // TODO: Remove when done using mock
//        usersModel.getUserHistory(token).enqueue(object : Callback<List<HistoryMessage>> {
        usersModel.getUserHistory(MOCK_TOKEN).enqueue(object : Callback<List<HistoryMessage>> {
            override fun onFailure(call: Call<List<HistoryMessage>>, t: Throwable) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }

            override fun onResponse(call: Call<List<HistoryMessage>>, response: Response<List<HistoryMessage>>) {
                if (response.isSuccessful) {

                    val historyMessages = response.body() ?: listOf()
                    val sortedHistoryMessages = historyMessages.sortedWith(compareBy({ it.Date.toLong() }))
                    val series = LineGraphSeries<DataPoint>(historyToDataPoint(sortedHistoryMessages).toTypedArray())

                    graph.title = "User Stock History"

                    graph.addSeries(series)
                } else {
                    Log.wtf(TAG, response.message().toString())
                }
            }
        })

    }

    // TODO: REPLACE SECOND PARAMETER WITH `DataPoint(msToDate(it.Date)`
    private fun historyToDataPoint(messages: List<HistoryMessage>): List<DataPoint> {
        val list = mutableListOf<DataPoint>()
        messages.forEach {
            list.add(DataPoint(num, it.Value))
            num += 10
        }
        return list
    }

    private fun msToDate(ms: String): Date = Date(ms.toLong())
}

