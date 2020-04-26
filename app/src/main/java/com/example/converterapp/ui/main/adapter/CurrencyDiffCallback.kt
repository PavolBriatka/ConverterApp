package com.example.converterapp.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency

class CurrencyDiffCallback(
    private val oldData: ArrayList<Currency>,
    private val newData: ArrayList<Currency>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].currencyCode == newData[newItemPosition].currencyCode
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].relativeRate == newData[newItemPosition].relativeRate
    }
}