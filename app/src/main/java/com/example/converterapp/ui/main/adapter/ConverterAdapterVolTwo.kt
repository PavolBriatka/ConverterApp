package com.example.converterapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.converterapp.R
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import com.example.converterapp.utils.AmountEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ConverterAdapterVolTwo :
    RecyclerView.Adapter<ConverterAdapterVolTwo.CurrencyViewHolder>() {

    var onItemClicked: ((Currency) -> Unit)? = null
    var currencyData = arrayListOf<Currency>()
    private var disposables = CompositeDisposable()

    private lateinit var sharedViewModel: MainViewModel

    fun setViewModel(viewModel: MainViewModel) {
        sharedViewModel = viewModel
    }

    fun setData(newData: ArrayList<Currency>) {

        if (currencyData.isEmpty()) {
            currencyData = newData
            notifyDataSetChanged()
        }

    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        CurrencyView {
        val currencyFlag: ImageView = itemView.findViewById(R.id.iv_currency_flag)
        val currencyCode: TextView = itemView.findViewById(R.id.tv_currency_code)
        val currencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
        val conversionValue: AmountEditText = itemView.findViewById(R.id.et_currency_value)

        private var disposables = CompositeDisposable()
        var subscription: Disposable? = null

        init {

            itemView.setOnClickListener {
                if (adapterPosition > 0) {
                    val itemToMove = currencyData[adapterPosition]

                    onItemClicked?.invoke(itemToMove)

                    currencyData.remove(itemToMove)
                    currencyData.add(0, itemToMove)
                    notifyItemMoved(adapterPosition, 0)
                    notifyItemRangeChanged(0, currencyData.size)
                }
            }
        }

        override fun updateCurrencyValue(value: Double) {
            conversionValue.setText(value.toString())
        }

        override fun clearSubscription() {
            if (subscription != null) subscription!!.dispose()
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

        if (holder.subscription != null) {
            holder.subscription!!.dispose()
        }

        sharedViewModel.getCurrencyData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                holder.conversionValue.setText(it[position].relativeRate.toString())
            }.let { holder.subscription = it }

        holder.currencyCode.text = currentItem.currencyCode
        holder.conversionValue.isEnabled = position == 0

    }

}