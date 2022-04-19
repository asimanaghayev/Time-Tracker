package com.nsp.timetracker.uikit.view

import android.text.Editable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.nsp.timetracker.support.watcher.EmptyTextWatcher
import java.math.BigDecimal

object AmountViewBindingsAdapter {

    @BindingAdapter(value = ["decimalValueAttrChanged"])
    @JvmStatic
    fun setListeners(view: AmountView, listener: InverseBindingListener) {
        view.addTextChangedListener(object : EmptyTextWatcher() {
            override fun afterTextChanged(editable: Editable) {
                listener.onChange()
            }
        })
    }

    @BindingAdapter("decimalValue")
    @JvmStatic
    fun setRealValue(view: AmountView, value: BigDecimal?) {
        if (value!=null && view.getValue() != value.toString()) {
            view.decimalValue = value
        }
    }

    @InverseBindingAdapter(attribute = "decimalValue", event = "decimalValueAttrChanged")
    @JvmStatic
    fun getRealValue(view: AmountView): BigDecimal {
        return view.decimalValue
    }
}