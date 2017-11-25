package com.stocks.stocks_io.POJO

data class Options(val Symbol: String, val Units: Int)

data class StockPrice(val latestPrice: Double, val symbol: String)

data class ExtendedOptions(val symbol: String, val units: Int, val price: Double)
