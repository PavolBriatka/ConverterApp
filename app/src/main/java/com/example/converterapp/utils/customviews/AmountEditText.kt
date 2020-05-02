package com.example.converterapp.utils.customviews

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class AmountEditText : TextInputEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {

        //set the selection when clicking on the field
        this.setOnClickListener {
            this.setSelection(this.text?.length ?: 0)
        }

        //set the selection when field gets focus
        this.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                this.post { this.setSelection(this.text?.length ?: 0) }
            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        setSelection(this.text?.length ?: 0)
    }
}