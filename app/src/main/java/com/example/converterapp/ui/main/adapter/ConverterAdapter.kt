package com.example.converterapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.converterapp.R
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import com.example.converterapp.utils.customviews.AmountEditText
import com.jakewharton.rxbinding3.widget.textChanges
import com.mikhaellopez.circularimageview.CircularImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.text.DecimalFormat

class ConverterAdapter :
    RecyclerView.Adapter<ConverterAdapter.CurrencyViewHolder>() {

    companion object ViewType {

        private const val BASE_CURRENCY_VIEW = 11
        private const val SECONDARY_CURRENCY_VIEW = 22
    }

    var onItemClicked: (() -> Unit)? = null
    var currencyData = arrayListOf<Currency>()

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

    fun hasData(): Boolean {
        return currencyData.isNotEmpty()
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyFlag: CircularImageView = itemView.findViewById(R.id.iv_currency_flag)
        val currencyCode: TextView = itemView.findViewById(R.id.tv_currency_code)
        val currencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
        val conversionValue: AmountEditText = itemView.findViewById(R.id.et_currency_value)

        var disposables = CompositeDisposable()

        fun moveItem(item: Currency) {
            currencyData.remove(item)
            currencyData.add(0, item)
            onItemClicked?.invoke()
            notifyItemMoved(adapterPosition, 0)
            notifyItemRangeChanged(0, currencyData.size)
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

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) BASE_CURRENCY_VIEW else SECONDARY_CURRENCY_VIEW
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {

        val currentItem = currencyData[position]

        with(holder) {

            currencyCode.text = currentItem.currencyCode
            currencyName.text = currentItem.currencyName
            currencyFlag.setImageResource(currentItem.flagId)
            conversionValue.isEnabled = position == 0

            disposables.clear()

            when (getItemViewType(position)) {
                BASE_CURRENCY_VIEW -> {
                    disposables.addAll(subscribeToUserInput { value ->
                        if (conversionValue.text.toString() != value.second) {
                            conversionValue.tag = "SET"
                            conversionValue.setText(value.second)
                        }
                    },
                        conversionValue.subscribeToTextChanges { input ->
                            if (conversionValue.tag == null) {
                                sharedViewModel.updateUserInput(
                                    Pair(
                                        currentItem.currencyCode,
                                        input.toString()
                                    )
                                )
                            }

                            conversionValue.tag = null
                        })
                    conversionValue.requestFocus()
                }
                else -> {
                    itemView.setOnClickListener {
                        sharedViewModel.updateUserInput(
                            Pair(
                                currentItem.currencyCode,
                                validateClickedItemValue(conversionValue.text.toString())
                            )
                        )
                        moveItem(currentItem)
                    }
                    disposables.add(subscribeToData { data ->
                        val value = data[currentItem.currencyCode]?.relativeRate ?: 0.0
                        conversionValue.setText(transformValue(value))
                    })
                }

            }
        }
    }

    private fun transformValue(value: Double): String {

        val partialTransform = DecimalFormat().apply {
            applyPattern("#,##0.00")
        }.format(value)

        return if (partialTransform.endsWith(".00") ||
            partialTransform.endsWith(",00")
        ) partialTransform.dropLast(3) else partialTransform
    }

    /*
     * Transform value of clicked item - if it's zero, the AmountEditText should appear empty
     * so the user does not have to manually clear "0.0" value.
     */
    private fun validateClickedItemValue(itemValue: String): String {
        return if (itemValue == "0.0" || itemValue == "0,0") "" else itemValue
    }

    /*
    * If the user tries to start input with "." or "," (visible on numeric keyboard) the value
    * is automatically transformed to "0." or "0," - on one hand it prevents the app from crashing,
    * on the other hand it provides better user experience.
    * Similarly, when the user starts with zero followed number and not by decimal point,
    * drop the zero in the front.
    */
    private fun validateInputFieldValue(input: CharSequence): CharSequence {
        return when {
            input.length >= 2 &&
            input.startsWith("0") &&
            !input.startsWith("0.") &&
            !input.startsWith("0,") -> input.substring(1, input.length)
            input.startsWith(".") -> "0."
            input.startsWith(",") -> "0,"
            else -> input
        }
    }

    private inline fun EditText.subscribeToTextChanges(crossinline toExecute: (input: CharSequence) -> Unit): Disposable {
        return this.textChanges()
            .skip(1)
            .subscribe {
                toExecute.invoke(validateInputFieldValue(it))
            }
    }

    private inline fun subscribeToUserInput(crossinline toExecute: (Pair<String, String>) -> Unit): Disposable {
        return sharedViewModel.getUserInput()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                toExecute.invoke(value)
            }
    }

    private inline fun subscribeToData(crossinline toExecute: (Map<String, Currency>) -> Unit): Disposable {
        return sharedViewModel.getCurrencyData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                toExecute.invoke(data)
            }
    }

}