package com.nsp.timetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nsp.timetracker.data.db.model.CategoryProject
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.databinding.FragmentHomeBinding
import com.nsp.timetracker.support.extensions.timeFormat
import com.nsp.timetracker.support.listener.EmptyListener
import com.nsp.timetracker.support.util.DateUtils
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.base.adapter.MultiClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override val isDarkActionBar: Boolean = true
    override val isHeaderEnabled: Boolean = false

    private var timer: Timer? = null

    private val categoryAdapter: CategoryAdapter =
        CategoryAdapter(object : ItemClickListener<CategoryProject> {
            override fun onItemClick(item: CategoryProject) {
                viewModel.addProject(item.category)
            }
        },
            object : MultiClickListener<Project> {
                override fun onItemClick(
                    item: Project,
                    position: Int,
                    @ProjectClickType clickType: Int,
                ) {
                    when (clickType) {
                        ProjectClickType.CLICK_START -> viewModel.startTimer(item)
                        ProjectClickType.CLICK_STOP -> viewModel.stopTimer(item)
                    }
                }
            }
        )

    private var ongoingAdapter: OngoingProjectAdapter =
        OngoingProjectAdapter(object : ItemClickListener<History> {
            override fun onItemClick(item: History) {
                viewModel.stopTimer(item)
            }
        })

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupCategoryAdapter()
        setupOngoingProjectAdapter()
    }

    private fun setupCategoryAdapter() {
        binding.rvCategories.adapter = categoryAdapter
        viewModel.categoryProjects.observe(viewLifecycleOwner) {
            categoryAdapter.setItems(it as ArrayList<CategoryProject>)
        }
    }

    private fun setupOngoingProjectAdapter() {
        viewModel.ongoingProjects.observe(viewLifecycleOwner) {
            ongoingAdapter.setItems(it)
            categoryAdapter.selectedItems = it
            ongoingAdapter.totalSeconds.value = 0

            for (i in 0 until binding.rvCategories.childCount) {
                val child = binding.rvCategories.getChildAt(i)
                (binding.rvCategories.getChildViewHolder(child) as CategoryAdapter.CategoryViewHolder)
                    .notifyProjectsUpdated()
            }
        }
        binding.rvOngoingProjects.adapter = ongoingAdapter
        ongoingAdapter.totalSeconds.observe(viewLifecycleOwner) {
            binding.totalTime.text = ongoingAdapter.totalSeconds.value?.timeFormat()
        }

        setupTimer()
    }

    private fun setupTimer() {
        timer?.cancel()
        timer = DateUtils.setupTimer(object : EmptyListener {
            override fun onAction() {
                ongoingAdapter.totalSeconds.apply {
                    postValue(value?.plus(ongoingAdapter.items.size))
                }
            }
        })
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@HomeFragment
        viewmodel = viewModel
    }
}