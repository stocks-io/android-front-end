package com.stocks.stocks_io.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.stocks.stocks_io.POJO.ExtendedOptions
import com.stocks.stocks_io.R

class OptionsAdapter(private val dataSet: List<ExtendedOptions>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    var stockClickLister: StockClickListener? = null

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.stockText?.text = dataSet[position].symbol
        holder?.unitsText?.text = dataSet[position].units.toString()
        holder?.moneyText?.text = "$${dataSet[position].price}"

        holder?.sharesView?.setOnClickListener {
            stockClickLister?.onStockClicked(dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.options_row, parent, false))


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var sharesView: LinearLayout? = null
        var moneyText: TextView? = null
        var stockText: TextView? = null
        var unitsText: TextView? = null

        init {
            sharesView = itemView?.findViewById(R.id.shares_view)
            moneyText = itemView?.findViewById(R.id.money_text)
            stockText = itemView?.findViewById(R.id.stock_text)
            unitsText = itemView?.findViewById(R.id.units_text)
        }
    }
}

interface StockClickListener {
    fun onStockClicked(option: ExtendedOptions)
}

