package com.stocks.stocks_io.POJO

data class StockHistory(val count: Int, val results: List<History>)

data class History(val high: Double)