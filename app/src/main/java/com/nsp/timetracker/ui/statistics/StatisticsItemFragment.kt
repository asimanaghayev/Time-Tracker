package com.nsp.timetracker.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.data.db.dao.model.StatisticsProject
import com.nsp.timetracker.databinding.FragmentStatisticsItemBinding
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.history.HistoryCategoryAdapter
import com.nsp.timetracker.ui.history.HistoryType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsItemFragment(private val type: Timing = Timing.DAILY) :
    BaseFragment<FragmentStatisticsItemBinding, StatisticsItemViewModel>() {

    enum class Timing {
        DAILY, WEEKLY, MONTHLY
    }

    override val viewModel: StatisticsItemViewModel by viewModels()

    override val isDarkActionBar: Boolean
        get() = true

    private var projectAdapter: StatisticsProjectAdapter =
        StatisticsProjectAdapter(object : ItemClickListener<StatisticsProject> {
            override fun onItemClick(item: StatisticsProject) {}
        })

    private var categoryAdapter: HistoryCategoryAdapter =
        HistoryCategoryAdapter(object : ItemClickListener<StatisticsCategory> {
            override fun onItemClick(item: StatisticsCategory) {}
        })

    val useCaseType = MutableLiveData(HistoryType.PROJECT)

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentStatisticsItemBinding {
        return FragmentStatisticsItemBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupStatsAdapter()
        setupUseCase()
        fetchData()
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@StatisticsItemFragment
        viewmodel = viewModel
    }

    private fun setupStatsAdapter() {
        viewModel.projectStats.observe(viewLifecycleOwner) {
            projectAdapter.setItems(it)
        }
        binding.rvProjects.adapter = projectAdapter


        viewModel.categoryStats.observe(viewLifecycleOwner) {
            categoryAdapter.setItems(it)
        }
        binding.rvCategories.adapter = categoryAdapter
    }

    private fun setupUseCase() {
        useCaseType.observe(viewLifecycleOwner) {
            binding.rvCategories.isVisible = it == HistoryType.CATEGORY
            binding.rvProjects.isVisible = it == HistoryType.PROJECT
        }
    }

    private fun fetchData() {
        viewModel.fetchStatistics(type)
    }
}