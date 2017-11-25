package com.stocks.stocks_io.Activities

import android.app.DialogFragment
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.stocks.stocks_io.Data.Endpoints
import com.stocks.stocks_io.Data.Endpoints.DEVBASEURL
import com.stocks.stocks_io.Model.PortfolioModel
import com.stocks.stocks_io.POJO.ExtendedOptions
import com.stocks.stocks_io.POJO.OrderRequest
import com.stocks.stocks_io.POJO.StockHistory
import com.stocks.stocks_io.R
import kotlinx.android.synthetic.main.fragment_popup_buy_sell.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PopupBuySellFragment : DialogFragment() {

    var options: ExtendedOptions? = null
    var dismissListener: (() -> Unit)? = null

    private val MOCK_TOKEN = "E48D1744-6042-4F8B-BC67-3E62B1AC77ED"
    private var isBuying = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View =
            LayoutInflater.from(activity).inflate(R.layout.fragment_popup_buy_sell, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        ticker_text.text = "${options?.symbol} $${options?.price}"
        options?.let {
            val immutableOptions = options ?: ExtendedOptions("", 1, 1.0)
            setUpGraph(immutableOptions)
            input_view.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) { }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var num: Int
                    s?.let {
                        num = if (s.isEmpty()) {
                            0
                        } else {
                            s.toString().toInt()
                        }

                        calculator_view.visibility = VISIBLE
                        calculator_view.text = "$num * $${options?.price} = $${num * (options?.price ?: 0.0)}"
                    }
                }
            })
        }
        buy_sell_buttons.setOnCheckedChangeListener { _, checkedId ->
            info_view.visibility = VISIBLE
            isBuying = when (checkedId) { R.id.buy_radio -> true
                R.id.sell_radio -> false
                else -> false
            }
        }

        activate_button.setOnClickListener {
            if (input_view.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Add how many stocks you want to buy or sell!", Toast.LENGTH_LONG).show()

            } else {
                val immutableOptions: ExtendedOptions = options ?: ExtendedOptions("", -1, -1.0)
                val price = input_view.text.toString().toInt()
                if (isBuying) {
                    buyStocks(immutableOptions.symbol, price)
                } else {
                    sellStocks(immutableOptions.symbol, price)
                }
            }
        }
    }

    private fun buyStocks(symbol: String, units: Int) {
//        val token = getUserToken(activity)
        val token = MOCK_TOKEN
        val retrofit = Retrofit.Builder()
                .baseUrl(Endpoints.DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val portfolioModel = retrofit.create(PortfolioModel::class.java)
        portfolioModel.buyStocks(token, units, symbol).enqueue(object : Callback<OrderRequest> {
            override fun onResponse(call: Call<OrderRequest>, response: Response<OrderRequest>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Successfully ordered!", Toast.LENGTH_LONG).show()
                    dismissListener?.invoke()
                    dismiss()
                } else {
                    Toast.makeText(activity, "No TSLA for u fam ${response.message()}", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<OrderRequest>, t: Throwable) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }
        })
    }

    private fun sellStocks(symbol: String, units: Int) {
//        val token = getUserToken(activity)
        val token = MOCK_TOKEN
        Log.wtf(TAG, token)

        val retrofit = Retrofit.Builder()
                .baseUrl(DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val portfolioModel = retrofit.create(PortfolioModel::class.java)
        portfolioModel.sellStocks(token, units, symbol).enqueue(object : Callback<OrderRequest> {
            override fun onResponse(call: Call<OrderRequest>, response: Response<OrderRequest>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Successfully sold!", Toast.LENGTH_LONG).show()
                    dismissListener?.invoke()
                    dismiss()
                } else {
                    Toast.makeText(activity, "KEEP YO TSLA FAM ${response.message()}", LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OrderRequest>, t: Throwable) {
                Log.wtf(TAG, "This is why I cry myself to sleep every night")
            }
        })

    }

    private fun setUpGraph(option: ExtendedOptions) {
        val retrofit = Retrofit.Builder()
                .baseUrl(DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val portfolioModel = retrofit.create(PortfolioModel::class.java)

        portfolioModel.getStockHistory(MOCK_TOKEN, option.symbol, "3m").enqueue(object : Callback<StockHistory> {
            override fun onResponse(call: Call<StockHistory>, response: Response<StockHistory>) {
                response.body()?.let {
                    val dataList = mutableListOf<DataPoint>()
                    var num = 0.0
                    it.results.forEach { result ->
                        dataList.add(DataPoint(num, result.high))
                        num += 10
                    }
                    performance_history.addSeries(LineGraphSeries<DataPoint>(dataList.toTypedArray()))
                }
            }

            override fun onFailure(call: Call<StockHistory>, t: Throwable) {
            }

        })
    }

    interface DismissListener {
        fun onDismissed()
    }
}