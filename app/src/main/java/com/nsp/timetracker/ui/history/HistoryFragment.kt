package com.nsp.timetracker.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.databinding.FragmentHistoryBinding
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.MultiClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<HistoryViewModel>() {

    private lateinit var binding: FragmentHistoryBinding

    override val viewModel: HistoryViewModel by viewModels()

    private var historyAdapter: HistoryAdapter =
        HistoryAdapter(object : MultiClickListener<History> {
            override fun onItemClick(item: History, position: Int, clickType: Int) {
                // TODO: not implemented yet
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_history,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupHistoryAdapter()
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@HistoryFragment
        viewmodel = viewModel
    }

    private fun setupHistoryAdapter() {
        viewModel.allHistory.observe(viewLifecycleOwner) {
            historyAdapter.setItems(it)
        }
        binding.rvHistory.adapter = historyAdapter
    }
}