package com.example.converterapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.converterapp.R
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.google.android.material.textfield.TextInputEditText

class ConverterAdapter :
RecyclerView.Adapter<ConverterAdapter.CurrencyViewHolder>(){

    var onItemClicked: ((Currency) -> Unit)? = null

    var currencyData = arrayListOf<Currency>()

    fun setData(newData: ArrayList<Currency>) {

        currencyData = newData
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyFlag: ImageView = itemView.findViewById(R.id.iv_currency_flag)
        val currencyCode: TextView = itemView.findViewById(R.id.tv_currency_code)
        val currencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
        val conversionValue: TextInputEditText = itemView.findViewById(R.id.et_currency_value)

        init {
            itemView.setOnClickListener {
                val itemToMove = currencyData[adapterPosition]
                onItemClicked?.invoke(itemToMove)

                currencyData.remove(itemToMove)
                currencyData.add(0, itemToMove)
                notifyItemMoved(adapterPosition,0)
                notifyItemRangeChanged(0, 2)
            }
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

        val currentItem = currencyData[position]

        holder.currencyCode.text = currentItem.currencyCode
        holder.conversionValue.apply {
            isEnabled = position == 0
            setText(currentItem.relativeRate.toString())
        }
    }
}