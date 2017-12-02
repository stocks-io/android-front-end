package com.stocks.stocks_io.Fragments

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.stocks.stocks_io.Adapters.PopupSearchFragmentAdapter
import com.stocks.stocks_io.Adapters.SearchClickListener
import com.stocks.stocks_io.Data.Endpoints.STOCKS_BASE_URL
import com.stocks.stocks_io.Model.StockPriceOptions
import com.stocks.stocks_io.POJO.ExtendedOptions
import com.stocks.stocks_io.POJO.Results
import com.stocks.stocks_io.R
import io.realm.Case
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_search_popup.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.properties.Delegates

class PopupSearchFragment : DialogFragment() {
    private var realm: Realm by Delegates.notNull()

    var searchAdapter: PopupSearchFragmentAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_search_popup, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        realm = Realm.getDefaultInstance()
        autocomplete_rview.layoutManager = LinearLayoutManager(activity)
        searchAdapter = PopupSearchFragmentAdapter(realm.where(Results::class.java).findAll())
        autocomplete_rview.adapter = searchAdapter

        stock_search_field.onFocusChangeListener = View.OnFocusChangeListener { v, _ ->
            v.post {
                val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(stock_search_field, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        stock_search_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchAdapter?.searchClickListener = object : SearchClickListener {
                    override fun onItemClicked(item: String) {
                        Thread {
                            val stocksAsync = Retrofit.Builder()
                                    .baseUrl(STOCKS_BASE_URL)
                                    .addConverterFactory(MoshiConverterFactory.create())
                                    .build()

                            val stocks = stocksAsync.create(StockPriceOptions::class.java)
                            val options = stocks.getStockPrice(item).execute().body()
                            val extendedOptions = ExtendedOptions(options?.symbol ?: "", 0, options?.latestPrice ?: -1.0)
                            Handler(Looper.getMainLooper()).post{
                                val buyFragment = PopupBuySellFragment()
                                buyFragment.options = extendedOptions
                                buyFragment.bgFragment = fragmentManager.findFragmentByTag("RETURN OF THE JEBI") as DialogFragment
                                buyFragment.show(fragmentManager, "HELP ME IM STUCK IN AN ANDROID FACTORY")
                            }
                        }.start()
                    }
                }

                var results = realm.where(Results::class.java)
                        .beginsWith("Symbol", s.toString() , Case.INSENSITIVE)
                        .findAll()

                searchAdapter?.updateSearchResults(results)

            }
        })
    }
}