package com.nsp.timetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.databinding.FragmentHomeBinding
import com.nsp.timetracker.support.listener.EmptyListener
import com.nsp.timetracker.support.util.DateUtils
import com.nsp.timetracker.support.util.timeFormat
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import com.nsp.timetracker.ui.base.adapter.MultiClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var binding: FragmentHomeBinding
    override val viewModel: HomeViewModel by viewModels()

    private var timer: Timer? = null

    private lateinit var categoryAdapter: CategoryAdapter
    private val projectAdapter: ProjectAdapter =
        ProjectAdapter(object : MultiClickListener<Project> {
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
        })

    private var ongoingAdapter: OngoingProjectAdapter =
        OngoingProjectAdapter(object : ItemClickListener<History> {
            override fun onItemClick(item: History) {
                viewModel.stopTimer(item)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupCategoryAdapter()
        setupProjectAdapter()
        setupOngoingProjectAdapter()
    }

    private fun setupCategoryAdapter() {
        viewModel.allCategories.observe(viewLifecycleOwner) {
            categoryAdapter = CategoryAdapter(requireContext(), it)
            binding.spinnerProjects.adapter = categoryAdapter
        }
        binding.spinnerProjects.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                categoryAdapter.getItem(position)?.let { viewModel.refreshProjects(it) }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setupProjectAdapter() {
        viewModel.allProjects.observe(viewLifecycleOwner) {
            projectAdapter.setItems(it)
        }
        binding.rvProjects.adapter = projectAdapter
    }

    private fun setupOngoingProjectAdapter() {
        viewModel.ongoingProjects.observe(viewLifecycleOwner) {
            ongoingAdapter.setItems(it)
            projectAdapter.selectedItems = it
            ongoingAdapter.totalSeconds.value = 0
            projectAdapter.notifyDataSetChanged()
        }
        binding.rvOngoingProjects.adapter = ongoingAdapter

        timer?.cancel()
        timer = DateUtils.setupTimer(object : EmptyListener {
            override fun onAction() {
                ongoingAdapter.totalSeconds.apply {
                    postValue(value?.plus(ongoingAdapter.items.size))
                }
            }
        })

        ongoingAdapter.totalSeconds.observe(viewLifecycleOwner) {
            binding.totalTime.text = ongoingAdapter.totalSeconds.value?.timeFormat()
        }
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@HomeFragment
        viewmodel = viewModel
    }
}