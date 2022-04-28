package com.nsp.timetracker.uikit.view

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.nsp.timetracker.R
import com.nsp.timetracker.databinding.ViewHeaderBinding
import com.nsp.timetracker.support.extensions.hideGroupViews

class Header : LinearLayout {
    private var binding: ViewHeaderBinding = ViewHeaderBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    fun setTitle(text: CharSequence) {
        binding.title.isVisible = text.isNotEmpty()
        binding.title.text = text
    }

    fun setTitle(@StringRes res: Int) {
        setTitle(resources.getText(res))
    }

    fun setTitleLogo(drawable: Drawable) {
        binding.titleLogo.isVisible
        binding.titleLogo.setImageDrawable(drawable)
    }

    fun resetButtons() = with(binding) {
        hideGroupViews(btnLeftIcon, btnRightIcon, btnRightIcon2, btnRightLabel)
    }

    private fun backClick(context: Context) {
        val activity: Activity = context as Activity
        activity.onBackPressed()
    }

    fun setLeftButton(@DrawableRes res: Int, onClick: OnClickListener?) =
        setLeftButton(ContextCompat.getDrawable(context, res), onClick)

    fun setLeftButton(drawable: Drawable?, onClick: OnClickListener?) =
        setupButton(binding.btnLeftIcon, drawable, onClick)

    fun hideLeftButton() {
        binding.btnLeftIcon.visibility = View.GONE
    }

    fun setRightButton(@DrawableRes res: Int, onClick: OnClickListener?) =
        setLeftButton(ContextCompat.getDrawable(context, res), onClick)

    fun setRightButton(drawable: Drawable?, onClick: OnClickListener?) =
        setupButton(binding.btnRightIcon, drawable, onClick)

    fun setRightButton2(@DrawableRes res: Int, onClick: OnClickListener?) =
        setRightButton2(ContextCompat.getDrawable(context, res), onClick)

    fun setRightButton2(drawable: Drawable?, onClick: OnClickListener?) =
        setupButton(binding.btnRightIcon2, drawable, onClick)

    fun setRightButtonLabel(@StringRes res: Int, onClick: OnClickListener?) =
        setupButtonLabel(binding.btnRightLabel, resources.getString(res), onClick)

    private fun setupButton(view: ImageView, drawable: Drawable?, onClick: OnClickListener?) =
        with(view) {
            setImageDrawable(drawable)
            setOnClickListener(onClick)
            visibility = View.VISIBLE
        }

    private fun setupButtonLabel(view: TextView, label: String?, onClick: OnClickListener?) =
        with(view) {
            text = label
            setOnClickListener(onClick)
            visibility = View.VISIBLE
        }

    fun setupTheme(isDarkActionBar: Boolean) {
        val backgroundColor: Int =
            if (isDarkActionBar) R.color.colorPrimary
            else R.color.white
        val foregroundColor: Int =
            if (isDarkActionBar) R.color.white
            else R.color.colorPrimary

        binding.root.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
        binding.btnLeftIcon.setColorFilter(
            ContextCompat.getColor(context, foregroundColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.btnRightLabel.setTextColor(ContextCompat.getColor(context, foregroundColor))
    }
}