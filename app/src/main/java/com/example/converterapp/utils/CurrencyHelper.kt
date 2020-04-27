package com.example.converterapp.utils

import android.content.res.Resources
import com.example.converterapp.R

class CurrencyHelper(res: Resources) {

    val currencyMap = mapOf(

        Pair("AUD", Pair(res.getString(R.string.aud_name), R.drawable.flag_australia)),
        Pair("BGN", Pair(res.getString(R.string.bgn_name), R.drawable.flag_bulgaria)),
        Pair("BRL", Pair(res.getString(R.string.brl_name), R.drawable.flag_brazil)),
        Pair("CAD", Pair(res.getString(R.string.cad_name), R.drawable.flag_canada)),
        Pair("CHF", Pair(res.getString(R.string.chf_name), R.drawable.flag_switzerland)),
        Pair("CNY", Pair(res.getString(R.string.cny_name), R.drawable.flag_china)),
        Pair("CZK", Pair(res.getString(R.string.czk_name), R.drawable.flag_czech_republic)),
        Pair("DKK", Pair(res.getString(R.string.dkk_name), R.drawable.flag_denmark)),
        Pair("EUR", Pair(res.getString(R.string.eur_name), R.drawable.flag_eu)),
        Pair("GBP", Pair(res.getString(R.string.gbp_name), R.drawable.flag_united_kingdom)),
        Pair("HKD", Pair(res.getString(R.string.hkd_name), R.drawable.flag_hong_kong)),
        Pair("HRK", Pair(res.getString(R.string.hrk_name), R.drawable.flag_croatia)),
        Pair("HUF", Pair(res.getString(R.string.huf_name), R.drawable.flag_hungary)),
        Pair("IDR", Pair(res.getString(R.string.idr_name), R.drawable.flag_indonesia)),
        Pair("ILS", Pair(res.getString(R.string.ils_name), R.drawable.flag_israel)),
        Pair("INR", Pair(res.getString(R.string.inr_name), R.drawable.flag_india)),
        Pair("ISK", Pair(res.getString(R.string.isk_name), R.drawable.flag_iceland)),
        Pair("JPY", Pair(res.getString(R.string.jpy_name), R.drawable.flag_japan)),
        Pair("KRW", Pair(res.getString(R.string.krw_name), R.drawable.flag_south_korea)),
        Pair("MXN", Pair(res.getString(R.string.mxn_name), R.drawable.flag_mexico)),
        Pair("MYR", Pair(res.getString(R.string.myr_name), R.drawable.flag_malaysia)),
        Pair("NOK", Pair(res.getString(R.string.nok_name), R.drawable.flag_norway)),
        Pair("NZD", Pair(res.getString(R.string.nzd_name), R.drawable.flag_new_zealand)),
        Pair("PHP", Pair(res.getString(R.string.php_name), R.drawable.flag_philippines)),
        Pair("PLN", Pair(res.getString(R.string.pln_name), R.drawable.flag_poland)),
        Pair("RON", Pair(res.getString(R.string.ron_name), R.drawable.flag_romania)),
        Pair("RUB", Pair(res.getString(R.string.rub_name), R.drawable.flag_russian_federation)),
        Pair("SEK", Pair(res.getString(R.string.sek_name), R.drawable.flag_sweden)),
        Pair("SGD", Pair(res.getString(R.string.sgd_name), R.drawable.flag_singapore)),
        Pair("THB", Pair(res.getString(R.string.thb_name), R.drawable.flag_thailand)),
        Pair("USD", Pair(res.getString(R.string.usd_name), R.drawable.flag_united_kingdom)),
        Pair("ZAR", Pair(res.getString(R.string.zar_name), R.drawable.flag_south_africa))

    )
}