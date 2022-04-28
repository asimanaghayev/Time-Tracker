package com.nsp.timetracker.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.databinding.ItemHistoryCategoryBinding
import com.nsp.timetracker.support.adapter.DateViewHolder
import com.nsp.timetracker.support.util.DateUtils.getDate
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.base.adapter.SectionedAdapter
import java.util.*

class HistoryCategoryAdapter(itemClickListener: ItemClickListener<StatisticsCategory>) :
    SectionedAdapter<StatisticsCategory, HistoryCategoryAdapter.HistoryViewHolder, DateViewHolder>(
        itemClickListener) {
    override fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_section_header, parent, false)
        return DateViewHolder(view, null)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_category, parent, false)
        return HistoryViewHolder(view,
            itemClickListener as ItemClickListener<StatisticsCategory>)
    }

    override fun onPlaceSectionBetweenItems(itemPosition: Int, nextItemPosition: Int): Boolean {
        val itemDate = getDate((items[itemPosition] as StatisticsCategory).date)
        val nextItemDate = getDate((items[nextItemPosition] as StatisticsCategory).date)
        return itemDate != nextItemDate
    }

    override fun onBindHeaderViewHolder(holder: DateViewHolder?, position: Int) {
        holder?.bind(Date((items[position] as StatisticsCategory).date), position)
    }

    override fun onBindItemViewHolder(holder: HistoryViewHolder?, position: Int) {
        holder?.bind(items[position] as StatisticsCategory, position)
    }

    class HistoryViewHolder internal constructor(
        view: View?,
        multiClickListener: ItemClickListener<StatisticsCategory>,
    ) : BaseViewHolder<StatisticsCategory>(view!!, multiClickListener) {
        private val binding: ItemHistoryCategoryBinding = ItemHistoryCategoryBinding.bind(itemView)

        override fun bind(item: StatisticsCategory, position: Int) {
            super.bind(item, position)
            binding.item = item
        }
    }
}