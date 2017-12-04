package com.stocks.stocks_io.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.stocks.stocks_io.Adapters.LeaderboardAdapter
import com.stocks.stocks_io.Data.Endpoints.DEVBASEURL
import com.stocks.stocks_io.Model.UsersModel
import com.stocks.stocks_io.POJO.UserScore
import com.stocks.stocks_io.R
import kotlinx.android.synthetic.main.activity_leaderboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        supportActionBar?.title = "Leaderboard"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getLeaderboard()
    }

    private fun getLeaderboard() {
        val retrofit = Retrofit.Builder()
                .baseUrl(DEVBASEURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val leaderboard = retrofit.create(UsersModel::class.java)
        leaderboard.getLeaderboard().enqueue(object : Callback<List<UserScore>> {
            override fun onResponse(call: Call<List<UserScore>>, response: Response<List<UserScore>>) {
                response.body()?.let {
                    leaderboard_view.layoutManager = LinearLayoutManager(applicationContext)
                    leaderboard_view.adapter = LeaderboardAdapter(it)
                }
            }

            override fun onFailure(call: Call<List<UserScore>>, t: Throwable?) {
                Log.wtf("no", "slb")
            }

        })
    }
}
