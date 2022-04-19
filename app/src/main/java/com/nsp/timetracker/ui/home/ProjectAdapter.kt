package com.nsp.timetracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.databinding.ItemProjectBinding
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.MultiClickListener

class ProjectAdapter(itemClickListener: MultiClickListener<Project>) :
    BaseAdapter<Project, BaseAdapter.BaseViewHolder<Project>>(itemClickListener) {

    var selectedItems: List<History> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<Project> {
        return ProjectViewHolder(
            ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            multiClickListener
        )
    }

    inner class ProjectViewHolder(
        private val binding: ItemProjectBinding,
        onCLick: MultiClickListener<Project>?,
    ) :
        BaseAdapter.BaseViewHolder<Project>(binding.root, onCLick) {

        override fun bind(item: Project, position: Int) {
            super.bind(item, position)
            binding.item = item

            if (selectedItems.isNotEmpty() && selectedItems.any { history -> history.project.id == item.id }) {
                binding.btnStart.setImageResource(R.drawable.ic_stop)
                binding.btnStart.setOnClickListener {
                    multiClickListener?.onItemClick(item, position, ProjectClickType.CLICK_STOP)
                }
            } else {
                binding.btnStart.setImageResource(R.drawable.ic_play)
                binding.btnStart.setOnClickListener {
                    multiClickListener?.onItemClick(item, position, ProjectClickType.CLICK_START)
                }
            }
        }
    }
}