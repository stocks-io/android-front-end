package com.stocks.stocks_io.Utils

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.stocks.stocks_io.R

fun getUserToken(context: Context): String {
    val preferences = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE)
    val token = preferences.getString(context.getString(R.string.token), "")
    Log.wtf(TAG, "Fun token: $token")
    return token
}
