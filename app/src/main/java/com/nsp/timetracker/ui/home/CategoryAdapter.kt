package com.nsp.timetracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.CategoryProject
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.databinding.ItemCategoryBinding
import com.nsp.timetracker.support.extensions.expandCollapse
import com.nsp.timetracker.support.extensions.setEndDrawable
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.base.adapter.MultiClickListener

class CategoryAdapter(
    itemClickListener: ItemClickListener<CategoryProject>,
    private val projectListener: MultiClickListener<Project>,
) :
    BaseAdapter<CategoryProject, BaseAdapter.BaseViewHolder<CategoryProject>>(itemClickListener) {

    var selectedItems: List<History> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<CategoryProject> {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        onCLick: ItemClickListener<CategoryProject>?,
    ) : BaseViewHolder<CategoryProject>(binding.root, onCLick) {

        lateinit var adapter: ProjectAdapter

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
            adapter = ProjectAdapter(projectListener)
            adapter.setItems(item.projects as ArrayList<Project>)
            if (!selectedItems.isNullOrEmpty()) adapter.selectedItems = selectedItems

            binding.rvProjects.adapter = adapter
        }

        fun notifyProjectsUpdated() {
            adapter.selectedItems = selectedItems
            adapter.notifyDataSetChanged()
        }
    }
}