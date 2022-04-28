package com.nsp.timetracker.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.databinding.ItemHistoryBinding
import com.nsp.timetracker.support.adapter.DateViewHolder
import com.nsp.timetracker.support.util.DateUtils.getDate
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.base.adapter.SectionedAdapter
import java.util.*

class HistoryAdapter(itemClickListener: ItemClickListener<History>) :
    SectionedAdapter<History, HistoryAdapter.HistoryViewHolder, DateViewHolder>(itemClickListener) {
    override fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_section_header, parent, false)
        return DateViewHolder(view, null)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view, itemClickListener as ItemClickListener<History>)
    }

    override fun onPlaceSectionBetweenItems(itemPosition: Int, nextItemPosition: Int): Boolean {
        val itemDate = getDate((items[itemPosition] as History).startDate)
        val nextItemDate = getDate((items[nextItemPosition] as History).startDate)
        return itemDate != nextItemDate
    }

    override fun onBindHeaderViewHolder(holder: DateViewHolder?, position: Int) {
        holder?.bind(Date((items[position] as History).startDate), position)
    }

    override fun onBindItemViewHolder(holder: HistoryViewHolder?, position: Int) {
        holder?.bind(items[position] as History, position)
    }

    class HistoryViewHolder internal constructor(
        view: View?,
        multiClickListener: ItemClickListener<History>,
    ) :
        BaseViewHolder<History>(view!!, multiClickListener) {
        private val binding: ItemHistoryBinding = ItemHistoryBinding.bind(itemView)

        override fun bind(item: History, position: Int) {
            super.bind(item, position)
            binding.item = item
            itemView.setOnClickListener { }
            binding.imgEdit.setOnClickListener { itemClickListener?.onItemClick(item) }
        }
    }
}