package com.example.converterapp.utils

import android.text.Editable
import android.text.TextWatcher


abstract class AmountFieldTextWatcher : TextWatcher {

    private var isAddDigit = false
    private var isRemoveDigit = false

    override fun afterTextChanged(s: Editable?) {
        when {
            isAddDigit -> {
                addDigit(s!!.last())
            }
            isRemoveDigit -> {
                removeDigit()
            }
            else -> {
                setText(s!!.toString())
            }
        }
        isAddDigit = false
        isRemoveDigit = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (start == s?.length && count == 0 && after == 1) {
            isAddDigit = true
        } else if (start + 1 == s?.length && count == 1 && after == 0) {
            isRemoveDigit = true
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    /**
     * Called when a digit was added to the end
     */
    abstract fun addDigit(digit: Char)

    /**
     * Called when the last digit was deleted
     */
    abstract fun removeDigit()

    /**
     * Called when the text changed and neither [addDigit] nor [removeDigit] is called.
     */
    abstract fun setText(text:String)
}