package com.stocks.stocks_io.POJO

data class OrderRequest(val totalCost: String, val remainingCash: Double, val msg: String): BaseMessage(msg)