package com.example.converterapp.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.converterapp.R
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.utils.AmountEditText
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable

class ConverterAdapter :
    RecyclerView.Adapter<ConverterAdapter.CurrencyViewHolder>() {

    var onItemClicked: ((Currency) -> Unit)? = null
    var currencyData = arrayListOf<Currency>()

    fun setData(newData: ArrayList<Currency>) {

        currencyData = newData
        notifyDataSetChanged()
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyFlag: ImageView = itemView.findViewById(R.id.iv_currency_flag)
        val currencyCode: TextView = itemView.findViewById(R.id.tv_currency_code)
        val currencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
        val conversionValue: AmountEditText = itemView.findViewById(R.id.et_currency_value)


        private var disposables = CompositeDisposable()
        var baseCurrency: Currency = currencyData[0]
        var baseValue: Double = 0.0

        init {

            itemView.setOnClickListener {
                if (adapterPosition > 0) {
                    val itemToMove = currencyData[adapterPosition]

                    onItemClicked?.invoke(itemToMove)
                    baseCurrency = itemToMove

                    currencyData.remove(itemToMove)
                    currencyData.add(0, itemToMove)
                    notifyItemMoved(adapterPosition, 0)
                    notifyItemRangeChanged(0, currencyData.size)
                }
            }

            //Add check for string 0 (either map{} or check in subscribe)
            conversionValue.textChanges()
                .filter {
                    it.isNotBlank() && adapterPosition == 0
                }
                .subscribe {
                    baseValue = it.toString().toDouble()
                }.let { disposables.add(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return currencyData.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {

        Log.e("BASECURRENCY", "${holder.baseCurrency.currencyCode} is initialized")

        val currentItem = currencyData[position]

        holder.currencyCode.text = currentItem.currencyCode
        holder.conversionValue.apply {
            isEnabled = position == 0
            val stringValue = currentItem.relativeRate.toString().removeSuffix(".0")
            setText(stringValue)
        }
    }
}