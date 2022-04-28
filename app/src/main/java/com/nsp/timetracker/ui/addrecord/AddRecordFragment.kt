package com.nsp.timetracker.ui.addrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nsp.timetracker.data.db.model.CategoryProject
import com.nsp.timetracker.databinding.FragmentAddRecordBinding
import com.nsp.timetracker.support.extensions.safeLet
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddRecordFragment : BaseFragment<FragmentAddRecordBinding, AddRecordViewModel>() {

    override val viewModel: AddRecordViewModel by viewModels()
    private val args: AddRecordFragmentArgs by navArgs()

    override val isDarkActionBar: Boolean
        get() = true
    
    private val adapter: SelectedCategoryAdapter =
        SelectedCategoryAdapter(object : ItemClickListener<CategoryProject> {
            override fun onItemClick(item: CategoryProject) {
                navigate(AddRecordFragmentDirections.actionToAddProject(category = item.category))
            }
        })

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddRecordBinding {
        return FragmentAddRecordBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupClicks()
        setupCategoryAdapter()
        setupArguments()
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@AddRecordFragment
        viewmodel = viewModel
        history = args.history
    }

    private fun setupClicks() {
        binding.btnAddRecord.setOnClickListener {
            safeLet(
                args.history,
                binding.dateStart.date,
                binding.dateEnd.date) { history, startDate, endDate ->
                viewModel.editRecord(history, startDate, endDate)
            } ?: run {
                safeLet(
                    adapter.selectedItem,
                    binding.dateStart.date,
                    binding.dateEnd.date
                ) { project, startDate, endDate ->
                    viewModel.addNewRecord(
                        project,
                        startDate,
                        endDate
                    )
                }
            }
        }
    }

    private fun setupCategoryAdapter() {
        binding.rvCategories.adapter = adapter
        viewModel.categoryProjects.observe(viewLifecycleOwner) {
            adapter.setItems(it as ArrayList<CategoryProject>)
        }
    }

    private fun setupArguments() = with(binding) {
        args.history?.let {
            dateEnd.date = Date(it.endDate)
            dateStart.date = Date(it.startDate)
        }
    }
}