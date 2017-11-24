package com.stocks.stocks_io.Activities

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.stocks.stocks_io.POJO.ExtendedOptions
import com.stocks.stocks_io.R
import kotlinx.android.synthetic.main.fragment_popup_buy_sell.*

class PopupBuySellFragment : DialogFragment() {

    var options: ExtendedOptions? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View =
            LayoutInflater.from(activity).inflate(R.layout.fragment_popup_buy_sell, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        ticker_text.text = "${options?.symbol} $${options?.price}"
        buy_sell_buttons.setOnCheckedChangeListener { group, checkedId ->
            info_view.visibility = VISIBLE
        }

        activate_button.setOnClickListener {
            if (input_view.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Add how many stocks you want to buy or sell!", Toast.LENGTH_LONG).show()
            }
        }
    }
}