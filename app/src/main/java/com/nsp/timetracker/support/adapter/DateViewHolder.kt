package com.nsp.timetracker.support.adapter

import android.view.View
import com.nsp.timetracker.databinding.ItemSectionHeaderBinding
import com.nsp.timetracker.support.util.DateUtils.getDate
import com.nsp.timetracker.support.util.DateUtils.getFormattedDate
import com.nsp.timetracker.ui.base.adapter.BaseAdapter.BaseViewHolder
import com.nsp.timetracker.ui.base.adapter.MultiClickListener
import java.util.*

class DateViewHolder(view: View?, multiClickListener: MultiClickListener<Date?>?) :
    BaseViewHolder<Date?>(
        view!!, multiClickListener) {

    private val binding: ItemSectionHeaderBinding = ItemSectionHeaderBinding.bind(itemView)

    fun bind(date: Date, position: Int) {
        super.bind(date, position)
        binding.header.text = getDate(date.time)
    }

}