package com.example.converterapp.utils

import android.content.Context
import com.example.converterapp.R

class CurrencyHelper(context: Context) {

    val currencyMap = mapOf(

        Pair("AUD", Pair("Australian Dollar", R.drawable.flag_australia)),
        Pair("BGN", Pair("Bulgarian Lev", R.drawable.flag_bulgaria)),
        Pair("BRL", Pair("Brazilian Real", R.drawable.flag_brazil)),
        Pair("CAD", Pair("Canadian Dollar", R.drawable.flag_canada)),
        Pair("CHF", Pair("Swiss Franc", R.drawable.flag_switzerland)),
        Pair("CNY", Pair("Chinese Yuan", R.drawable.flag_china)),
        Pair("CZK", Pair("Czech Koruna", R.drawable.flag_czech_republic)),
        Pair("DKK", Pair("Danish Krone", R.drawable.flag_denmark)),
        Pair("EUR", Pair("Euro", R.drawable.flag_eu)),
        Pair("GBP", Pair("British Pound", R.drawable.flag_united_kingdom)),
        Pair("HKD", Pair("Hong Kong Dollar", R.drawable.flag_hong_kong)),

        Pair("HRK", Pair("Croatian Kuna", R.drawable.flag_croatia)),
        Pair("HUF", Pair("Hungarian Forint", R.drawable.flag_hungary)),
        Pair("IDR", Pair("Indonesian Rupiah", R.drawable.flag_indonesia)),
        Pair("ILS", Pair("Israeli New Shekel", R.drawable.flag_israel)),
        Pair("INR", Pair("Indian Rupee", R.drawable.flag_india)),
        Pair("ISK", Pair("Icelandic Króna", R.drawable.flag_iceland)),
        Pair("JPY", Pair("Japanese Yen", R.drawable.flag_japan)),
        Pair("KRW", Pair("South Korean Won", R.drawable.flag_south_korea)),
        Pair("MXN", Pair("Mexican Peso", R.drawable.flag_mexico)),
        Pair("MYR", Pair("Malaysian Ringgit", R.drawable.flag_malaysia)),
        Pair("NOK", Pair("Norwegian Krone", R.drawable.flag_norway)),

        Pair("NZD", Pair("New Zealand Dollar", R.drawable.flag_new_zealand)),
        Pair("PHP", Pair("Philippine Peso", R.drawable.flag_philippines)),
        Pair("PLN", Pair("Poland Złoty", R.drawable.flag_poland)),
        Pair("RON", Pair("Romanian Leu", R.drawable.flag_romania)),
        Pair("RUB", Pair("Russian Ruble", R.drawable.flag_russian_federation)),
        Pair("SEK", Pair("Swedish Krona", R.drawable.flag_sweden)),
        Pair("SGD", Pair("Singapore Dollar", R.drawable.flag_singapore)),
        Pair("THB", Pair("Thai Baht", R.drawable.flag_thailand)),
        Pair("USD", Pair("United States Dollar", R.drawable.flag_united_kingdom)),
        Pair("ZAR", Pair("South African Rand", R.drawable.flag_south_africa))

    )
}