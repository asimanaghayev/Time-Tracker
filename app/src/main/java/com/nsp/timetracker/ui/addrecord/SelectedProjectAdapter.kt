package com.nsp.timetracker.ui.addrecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.databinding.ItemProjectBinding
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.ItemClickListener

class SelectedProjectAdapter(itemClickListener: ItemClickListener<Project>) :
    BaseAdapter<Project, BaseAdapter.BaseViewHolder<Project>>(itemClickListener) {

    var selectedItem: Project? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<Project> {
        return ProjectViewHolder(
            ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    inner class ProjectViewHolder(
        private val binding: ItemProjectBinding,
        onCLick: ItemClickListener<Project>?,
    ) :
        BaseAdapter.BaseViewHolder<Project>(binding.root, onCLick) {

        override fun bind(item: Project, position: Int) {
            super.bind(item, position)
            binding.item = item

            if (selectedItem?.id == item.id) {
                binding.btnStart.setImageResource(R.drawable.ic_check)
                binding.btnStart.visibility = View.VISIBLE
            } else {
                binding.btnStart.visibility = View.INVISIBLE
            }
        }
    }
}