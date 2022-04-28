package com.nsp.timetracker.uikit.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.nsp.timetracker.R
import com.nsp.timetracker.databinding.ViewAmountBinding
import com.nsp.timetracker.support.filter.DecimalDigitsInputFilter
import com.nsp.timetracker.support.extensions.round
import java.math.BigDecimal

class AmountView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding = ViewAmountBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        applyAttributes(attrs)
        setOnClickListener {
            binding.value.requestFocus()
            //            value.setShowSoftInputOnFocus(true);
            val inputMethodManager =
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.value, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.value.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter())
        setupButtons()
    }

    private fun setupButtons() {
        setStartIcon(R.drawable.ic_minus)
        setEndIcon(R.drawable.ic_plus)
        setStartIconListener {
            decimalValue.let {
                if (it > BigDecimal.ZERO) {
                    decimalValue = decimalValue.minus(BigDecimal(0.01)).round()
                }
            }
        }
        setEndIconListener {
            decimalValue = decimalValue.add(BigDecimal(0.01)).round()
        }
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val styles = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AmountView, 0, 0
        )
        try {
            setValue(styles.getString(R.styleable.AmountView_value))
            setTitle(styles.getString(R.styleable.AmountView_hint))
        } finally {
            styles.recycle()
        }
    }

    var decimalValue: BigDecimal
        get() = binding.value.text.toString().let {
            return if (it.isNotEmpty()) it.toBigDecimal()
            else BigDecimal.ZERO
        }
        set(value) = value.let {
            setValue(it.toString())
        }


    fun getValue(): String {
        return binding.value.text.toString()
    }

    fun setValue(value: String?) {
        binding.value.setText(value)
    }

    /**
     * Hint methods
     */

    open fun getTitle(): String {
        return binding.inputLayout.hint.toString()
    }

    open fun setTitle(text: String?) {
        binding.inputLayout.hint = text
    }

    /**
     * Start Icon methods
     */
    fun setStartIcon(@DrawableRes resId: Int) {
        setStartIcon(ContextCompat.getDrawable(context, resId))
    }

    fun setStartIcon(drawable: Drawable?) {
        binding.inputLayout.startIconDrawable = drawable
    }

    fun setStartIconListener(listener: OnClickListener?) {
        binding.inputLayout.setStartIconOnClickListener(listener)
    }

    /**
     * End Icon methods
     */
    fun setEndIcon(@DrawableRes resId: Int) {
        setEndIcon(ContextCompat.getDrawable(context, resId))
    }

    fun setEndIcon(icon: Drawable?) {
        binding.inputLayout.isEndIconVisible = true
        binding.inputLayout.endIconDrawable = icon
    }

    fun setEndIconListener(listener: OnClickListener?) {
        binding.inputLayout.setEndIconOnClickListener(listener)
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        binding.value.addTextChangedListener(watcher)
    }
}