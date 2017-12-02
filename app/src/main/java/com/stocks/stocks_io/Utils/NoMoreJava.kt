package com.stocks.stocks_io.Utils

import android.util.Log
import com.stocks.stocks_io.Data.Endpoints.DEVBASEURL
import com.stocks.stocks_io.Model.PortfolioModel
import com.stocks.stocks_io.POJO.Symbols
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.properties.Delegates

fun getAndSaveSymbols() {
    val retrofit = Retrofit.Builder()
            .baseUrl(DEVBASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val symbols = retrofit.create(PortfolioModel::class.java)
    symbols.getAllSymbols().enqueue(object : Callback<Symbols> {
        override fun onFailure(call: Call<Symbols>, t: Throwable) {
            Log.wtf("FUCK IT UP ", "MY SONS")
        }

        override fun onResponse(call: Call<Symbols>, response: Response<Symbols>) {
            var realm: Realm by Delegates.notNull()
            realm = Realm.getDefaultInstance()

            val immutableResponse = response.body()?.results ?: listOf()
            realm.executeTransaction {
                realm.insert(immutableResponse)
            }
        }
    })
}