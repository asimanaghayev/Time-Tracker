package com.nsp.timetracker.uikit.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.nsp.timetracker.R
import com.nsp.timetracker.databinding.ViewRecyclerCustomBinding
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.BaseAdapter.ItemChangeListener
import com.facebook.shimmer.ShimmerFrameLayout

class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val DEFAULT_SHIMMER_COUNT = 3
    var binding: ViewRecyclerCustomBinding =
        ViewRecyclerCustomBinding.inflate(LayoutInflater.from(context), this)
    private var shimmerId = 0
    private var shimmerCount = DEFAULT_SHIMMER_COUNT

    init {
        applyAttributes(attrs)
        initShimmerLayout()
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val styles = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RecyclerCustomView, 0, 0
        )
        try {
            binding.emptyView.setImage(styles.getDrawable(R.styleable.RecyclerCustomView_emptyImage))
            binding.emptyView.setText(styles.getString(R.styleable.RecyclerCustomView_emptyText))
            shimmerId =
                styles.getResourceId(R.styleable.RecyclerCustomView_shimmerLayout, shimmerId)
            shimmerCount = styles.getInt(R.styleable.RecyclerCustomView_shimmerCount, shimmerCount)
        } finally {
            styles.recycle()
        }
    }

    private fun initShimmerLayout() {
        if (shimmerId != 0) {
            for (j in 0 until shimmerCount) {
                val li = LayoutInflater.from(context)
                val shimmer = li.inflate(shimmerId, null, false)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                val padding = resources.getDimension(R.dimen.padding_small)
                    .toInt()
                params.setMargins(padding,
                    padding,
                    padding,
                    padding)
                shimmer.layoutParams = params
                binding.shimmerContainer.addView(shimmer)
            }
        }
    }

    val recyclerView: RecyclerView
        get() = binding.recyclerView
    val emptyView: RecyclerEmptyView
        get() = binding.emptyView
    val shimmerView: ShimmerFrameLayout
        get() = binding.shimmerView

    fun setAdapter(adapter: BaseAdapter<*, *>) {
        adapter.setEmptyView(binding.emptyView)
        adapter.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChange() {
                hideProgress()
            }
        })
        binding.recyclerView.adapter = adapter
        showProgress()
    }

    fun setEmptyText(text: String?) {
        binding.emptyView.setText(text)
    }

    fun setEmptyImage(drawable: Drawable?) {
        binding.emptyView.setImage(drawable)
    }

    fun showProgress() {
        if (binding.recyclerView.adapter == null || binding.recyclerView.adapter!!.itemCount == 0) {
            binding.emptyView.visibility = GONE
            binding.shimmerView.startShimmer()
            binding.shimmerView.visibility = VISIBLE
        }
    }

    fun hideProgress() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = GONE
    }
}