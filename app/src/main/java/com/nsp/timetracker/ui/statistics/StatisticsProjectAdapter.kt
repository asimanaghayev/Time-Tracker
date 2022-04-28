package com.nsp.timetracker.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.dao.model.StatisticsProject
import com.nsp.timetracker.databinding.ItemStatisticsProjectBinding
import com.nsp.timetracker.support.adapter.DateViewHolder
import com.nsp.timetracker.support.util.DateUtils.getDate
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.base.adapter.SectionedAdapter
import java.util.*

class StatisticsProjectAdapter(itemClickListener: ItemClickListener<StatisticsProject>) :
    SectionedAdapter<StatisticsProject, StatisticsProjectAdapter.HistoryViewHolder, DateViewHolder>(
        itemClickListener) {
    override fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_section_header, parent, false)
        return DateViewHolder(view, null)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistics_project, parent, false)
        return HistoryViewHolder(view, itemClickListener as ItemClickListener<StatisticsProject>)
    }

    override fun onPlaceSectionBetweenItems(itemPosition: Int, nextItemPosition: Int): Boolean {
        val itemDate = getDate((items[itemPosition] as StatisticsProject).date)
        val nextItemDate = getDate((items[nextItemPosition] as StatisticsProject).date)
        return itemDate != nextItemDate
    }

    override fun onBindHeaderViewHolder(holder: DateViewHolder?, position: Int) {
        holder?.bind(Date((items[position] as StatisticsProject).date), position)
    }

    override fun onBindItemViewHolder(holder: HistoryViewHolder?, position: Int) {
        holder?.bind(items[position] as StatisticsProject, position)
    }

    class HistoryViewHolder internal constructor(
        view: View?,
        multiClickListener: ItemClickListener<StatisticsProject>,
    ) :
        BaseViewHolder<StatisticsProject>(view!!, multiClickListener) {
        private val binding: ItemStatisticsProjectBinding =
            ItemStatisticsProjectBinding.bind(itemView)

        override fun bind(item: StatisticsProject, position: Int) {
            super.bind(item, position)
            binding.item = item
            itemView.setOnClickListener { }
        }
    }
}