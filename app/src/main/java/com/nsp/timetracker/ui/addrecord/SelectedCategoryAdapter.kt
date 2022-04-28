package com.nsp.timetracker.ui.addrecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.CategoryProject
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.databinding.ItemCategoryBinding
import com.nsp.timetracker.support.extensions.expandCollapse
import com.nsp.timetracker.support.extensions.setEndDrawable
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.ItemClickListener

class SelectedCategoryAdapter(
    itemClickListener: ItemClickListener<CategoryProject>,
) :
    BaseAdapter<CategoryProject, BaseAdapter.BaseViewHolder<CategoryProject>>(itemClickListener) {

    var selectedItem: Project? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<CategoryProject> {
        return SelectedCategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    inner class SelectedCategoryViewHolder(
        private val binding: ItemCategoryBinding,
        onCLick: ItemClickListener<CategoryProject>?,
    ) : BaseAdapter.BaseViewHolder<CategoryProject>(binding.root, onCLick) {

        override fun bind(item: CategoryProject, position: Int) {
            super.bind(item, position)
            binding.item = item

            setupProjectAdapter(item)
            binding.labelName.setOnClickListener {
                binding.labelName.setEndDrawable(
                    if (binding.rvProjects.isVisible) {
                        R.drawable.ic_arrow_down
                    } else {
                        R.drawable.ic_arrow_up
                    }
                )
                binding.rvProjects.expandCollapse()
            }
            binding.labelAddProject.setOnClickListener {
                itemClickListener?.onItemClick(item)
            }
        }

        private fun setupProjectAdapter(item: CategoryProject) {
            val adapter = SelectedProjectAdapter(object : ItemClickListener<Project> {
                override fun onItemClick(item: Project) {
                    selectedItem = item
                    notifyDataSetChanged()
                }
            })
            adapter.setItems(item.projects as ArrayList<Project>)
            if (selectedItem != null) adapter.selectedItem = selectedItem

            binding.rvProjects.adapter = adapter
        }
    }

}