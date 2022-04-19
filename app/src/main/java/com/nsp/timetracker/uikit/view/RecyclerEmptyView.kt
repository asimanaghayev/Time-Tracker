package com.nsp.timetracker.uikit.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.nsp.timetracker.R
import com.nsp.timetracker.databinding.ViewRecyclerEmptyBinding

class RecyclerEmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewRecyclerEmptyBinding =
        ViewRecyclerEmptyBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val styles = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RecyclerEmptyView, 0, 0
        )
        try {
            setText(styles.getString(R.styleable.RecyclerEmptyView_text))
            setImage(styles.getDrawable(R.styleable.RecyclerEmptyView_image))
        } finally {
            styles.recycle()
        }
    }

    fun setText(text: String?) {
        binding.text.text = text
    }

    fun setImage(drawable: Drawable?) {
        binding.image.setImageDrawable(drawable)
    }
}