package com.example.converterapp.utils

import android.content.Context
import com.example.converterapp.R

class CurrencyHelper(context: Context) : ICurrencyHelper {

    private val currencyMap = mapOf(

        Pair("AUD", Pair(context.getString(R.string.aud_name), R.drawable.flag_australia)),
        Pair("BGN", Pair(context.getString(R.string.bgn_name), R.drawable.flag_bulgaria)),
        Pair("BRL", Pair(context.getString(R.string.brl_name), R.drawable.flag_brazil)),
        Pair("CAD", Pair(context.getString(R.string.cad_name), R.drawable.flag_canada)),
        Pair("CHF", Pair(context.getString(R.string.chf_name), R.drawable.flag_switzerland)),
        Pair("CNY", Pair(context.getString(R.string.cny_name), R.drawable.flag_china)),
        Pair("CZK", Pair(context.getString(R.string.czk_name), R.drawable.flag_czech_republic)),
        Pair("DKK", Pair(context.getString(R.string.dkk_name), R.drawable.flag_denmark)),
        Pair("EUR", Pair(context.getString(R.string.eur_name), R.drawable.flag_eu)),
        Pair("GBP", Pair(context.getString(R.string.gbp_name), R.drawable.flag_united_kingdom)),
        Pair("HKD", Pair(context.getString(R.string.hkd_name), R.drawable.flag_hong_kong)),
        Pair("HRK", Pair(context.getString(R.string.hrk_name), R.drawable.flag_croatia)),
        Pair("HUF", Pair(context.getString(R.string.huf_name), R.drawable.flag_hungary)),
        Pair("IDR", Pair(context.getString(R.string.idr_name), R.drawable.flag_indonesia)),
        Pair("ILS", Pair(context.getString(R.string.ils_name), R.drawable.flag_israel)),
        Pair("INR", Pair(context.getString(R.string.inr_name), R.drawable.flag_india)),
        Pair("ISK", Pair(context.getString(R.string.isk_name), R.drawable.flag_iceland)),
        Pair("JPY", Pair(context.getString(R.string.jpy_name), R.drawable.flag_japan)),
        Pair("KRW", Pair(context.getString(R.string.krw_name), R.drawable.flag_south_korea)),
        Pair("MXN", Pair(context.getString(R.string.mxn_name), R.drawable.flag_mexico)),
        Pair("MYR", Pair(context.getString(R.string.myr_name), R.drawable.flag_malaysia)),
        Pair("NOK", Pair(context.getString(R.string.nok_name), R.drawable.flag_norway)),
        Pair("NZD", Pair(context.getString(R.string.nzd_name), R.drawable.flag_new_zealand)),
        Pair("PHP", Pair(context.getString(R.string.php_name), R.drawable.flag_philippines)),
        Pair("PLN", Pair(context.getString(R.string.pln_name), R.drawable.flag_poland)),
        Pair("RON", Pair(context.getString(R.string.ron_name), R.drawable.flag_romania)),
        Pair("RUB", Pair(context.getString(R.string.rub_name), R.drawable.flag_russia)),
        Pair("SEK", Pair(context.getString(R.string.sek_name), R.drawable.flag_sweden)),
        Pair("SGD", Pair(context.getString(R.string.sgd_name), R.drawable.flag_singapore)),
        Pair("THB", Pair(context.getString(R.string.thb_name), R.drawable.flag_thailand)),
        Pair("USD", Pair(context.getString(R.string.usd_name), R.drawable.flag_usa)),
        Pair("ZAR", Pair(context.getString(R.string.zar_name), R.drawable.flag_south_africa))

    )

    override fun fetchResources(code: String): Pair<String, Int>? {
        return currencyMap[code]
    }
}