package com.stocks.stocks_io.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.stocks.stocks_io.POJO.UserScore
import com.stocks.stocks_io.R

class LeaderboardAdapter(private val dataSet: List<UserScore>): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.userEmail?.text = dataSet[position].Email
        holder?.userMoney?.text = "$${dataSet[position].Cash}"
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.leaderboard_row, parent, false))


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var userEmail: TextView? = null
        var userMoney: TextView? = null

        init {
            userEmail = itemView?.findViewById(R.id.user_email)
            userMoney = itemView?.findViewById(R.id.user_money)
        }
    }
}
