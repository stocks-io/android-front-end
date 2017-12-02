package com.stocks.stocks_io.POJO

import io.realm.RealmObject

open class Symbols(var count: Int, var results: List<Results>)

open class Results(var Symbol: String, var Name: String, var industry: String): RealmObject() {
    constructor() : this("", "", "")
}