package com.nsp.timetracker.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.nsp.timetracker.R
import com.nsp.timetracker.databinding.FragmentStatisticsBinding
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.history.HistoryType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding, StatisticsItemViewModel>() {

    override val viewModel: StatisticsItemViewModel by viewModels()

    override val isDarkActionBar: Boolean
        get() = true

    lateinit var adapter: PagerAdapter

    private val historyTypes: Array<String> by lazy {
        resources.getStringArray(R.array.history_type)
    }

    private val tabTitles by lazy {
        resources.getStringArray(R.array.timing_type)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentStatisticsBinding {
        return FragmentStatisticsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupPagerFragments()
        setupSpinner()
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@StatisticsFragment
        viewmodel = viewModel
    }

    private fun setupPagerFragments() = with(binding.tabsPager) {
        activity?.let {
            this@StatisticsFragment.adapter = PagerAdapter(it.supportFragmentManager, lifecycle)
        }

        this@StatisticsFragment.adapter.apply {
            addFragment(StatisticsItemFragment(StatisticsItemFragment.Timing.DAILY))
            addFragment(StatisticsItemFragment(StatisticsItemFragment.Timing.WEEKLY))
            addFragment(StatisticsItemFragment(StatisticsItemFragment.Timing.MONTHLY))
        }
        adapter = this@StatisticsFragment.adapter

        TabLayoutMediator(binding.tabs, this) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setupSpinner() = with(binding.spinnerHistoryType) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                this@StatisticsFragment.adapter.fragmentList.forEach {
                    (it as StatisticsItemFragment).useCaseType.postValue(HistoryType.values()[position])
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val typeAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            context,
            android.R.layout.simple_spinner_item,
            historyTypes
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter = typeAdapter
    }

}