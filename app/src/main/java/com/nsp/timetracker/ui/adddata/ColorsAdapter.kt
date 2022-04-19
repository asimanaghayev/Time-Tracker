package com.nsp.timetracker.ui.adddata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import com.nsp.timetracker.databinding.ItemColorBinding
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.ItemClickListener

class ColorsAdapter(itemClickListener: ItemClickListener<Int>) :
    BaseAdapter<Int, BaseAdapter.BaseViewHolder<Int>>(itemClickListener) {

    var selectedItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Int> {
        return ColorViewHolder(
            ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    inner class ColorViewHolder(
        private val binding: ItemColorBinding,
        onCLick: ItemClickListener<Int>?,
    ) :
        BaseAdapter.BaseViewHolder<Int>(binding.root, onCLick) {

        override fun bind(@ColorRes item: Int, position: Int) {
            super.bind(item, position)
            binding.item = item

            binding.badgeSelection.visibility =
                if (selectedItemPosition == position) View.VISIBLE
                else View.INVISIBLE

            itemView.setOnClickListener {
                val oldPosition = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedItemPosition)
                itemClickListener?.onItemClick(item)
            }
        }

    }
}