package com.stocks.stocks_io

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.stocks.stocks_io.POJO.ExtendedOptions

class OptionsAdapter(private val dataSet: List<ExtendedOptions>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: OptionsAdapter.ViewHolder?, position: Int) {
        holder?.stockText?.text = dataSet[position].symbol
        holder?.unitsText?.text = dataSet[position].units.toString()
        holder?.moneyText?.text = "$${dataSet[position].price}"

    }

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OptionsAdapter.ViewHolder =
            OptionsAdapter.ViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.options_row, parent, false))


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var moneyText: TextView? = null
        var stockText: TextView? = null
        var unitsText: TextView? = null

        init {
            moneyText = itemView?.findViewById(R.id.money_text)
            stockText = itemView?.findViewById(R.id.stock_text)
            unitsText = itemView?.findViewById(R.id.units_text)
        }
    }
}
