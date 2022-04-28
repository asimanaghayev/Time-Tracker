package com.nsp.timetracker.ui.adddata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.nsp.timetracker.R
import com.nsp.timetracker.databinding.FragmentAddBinding
import com.nsp.timetracker.ui.base.BaseFragment
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding, AddViewModel>() {

    override val viewModel: AddViewModel by viewModels()
    private val args: AddFragmentArgs by navArgs()

    override val isDarkActionBar: Boolean
         get() = true

    private val adapter: ColorsAdapter = ColorsAdapter(object : ItemClickListener<Int> {
        override fun onItemClick(item: Int) {
            viewModel.selectedColor.postValue(item)
        }
    })

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
        setupColors()
    }

    private fun setupColors() {
        binding.rvColors.adapter = adapter
        binding.rvColors.layoutManager = GridLayoutManager(context, 4)
        val palette = resources.getIntArray(R.array.palette)
        adapter.setItems(palette.toList())
        viewModel.selectedColor.postValue(adapter.items[adapter.selectedItemPosition])
    }

    private fun bindUi(): Unit = with(binding) {
        lifecycleOwner = this@AddFragment
        viewmodel = viewModel
        isCategory = args.isCategory
        category = args.category
    }
}