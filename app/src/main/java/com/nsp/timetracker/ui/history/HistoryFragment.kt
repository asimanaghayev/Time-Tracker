package com.nsp.timetracker.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.databinding.FragmentHistoryBinding
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override val viewModel: HistoryViewModel by viewModels()

    override val isDarkActionBar: Boolean
        get() = true

    private val types: Array<String> by lazy {
        resources.getStringArray(R.array.history_type)
    }

    private var projectAdapter: HistoryAdapter =
        HistoryAdapter(object : ItemClickListener<History> {
            override fun onItemClick(item: History) {
                navigate(HistoryFragmentDirections.actionToEditRecord(item))
            }
        })

    private var categoryAdapter: HistoryCategoryAdapter =
        HistoryCategoryAdapter(object : ItemClickListener<StatisticsCategory> {
            override fun onItemClick(item: StatisticsCategory) {}
        })

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupClicks()
        setupSpinner()
        setupHistoryAdapter()
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@HistoryFragment
        viewmodel = viewModel
    }

    private fun setupClicks() {
        binding.addClick = View.OnClickListener {
            navigate(HistoryFragmentDirections.actionToAddRecord())
        }
    }

    private fun setupSpinner() = with(binding.spinnerHistoryType) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                binding.rvHistoryCategory.isVisible = position == HistoryType.CATEGORY.ordinal
                binding.rvHistoryProject.isVisible = position == HistoryType.PROJECT.ordinal
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val typeAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            context,
            android.R.layout.simple_spinner_item,
            types)
        typeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        adapter = typeAdapter
    }

    private fun setupHistoryAdapter() {
        viewModel.allHistoryProject.observe(viewLifecycleOwner) {
            projectAdapter.setItems(it)
        }
        binding.rvHistoryProject.adapter = projectAdapter

        viewModel.allStatisticsCategory.observe(viewLifecycleOwner) {
            categoryAdapter.setItems(it)
        }
        binding.rvHistoryCategory.adapter = categoryAdapter
    }

    override fun setupActionBar() {
        super.setupActionBar()

        getHeader().setRightButtonLabel(R.string.btn_statistics) {
            navigate(HistoryFragmentDirections.actionToStatistics())
        }
    }
}