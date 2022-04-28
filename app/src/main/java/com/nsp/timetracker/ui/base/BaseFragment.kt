package com.nsp.timetracker.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.nsp.timetracker.R
import com.nsp.timetracker.support.extensions.setupStatusBarColor
import com.nsp.timetracker.support.extensions.toast
import com.nsp.timetracker.support.tools.NavigationCommand
import com.nsp.timetracker.uikit.dialog.LoadingDialog
import com.nsp.timetracker.uikit.view.Header
import java.lang.UnknownError

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected abstract val viewModel: VM

    protected open val loadingDialog: DialogFragment? by lazy { LoadingDialog.build() }

    protected open val noInternetDialog: DialogFragment? by lazy {
        BaseBottomSheetDialog.build(
            title = R.string.error_no_internet_title,
            text = R.string.error_no_internet_text,
            image = R.drawable.ic_alert_no_internet,
            action = { it.dismiss() }
        )
    }

    protected open val isDarkActionBar: Boolean = false
    protected open val isHeaderEnabled: Boolean = true

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindNavController()
        bindCommonEffects()
        bindMessage()
        bindLoading()
    }

    override fun onStart() {
        super.onStart()
        setupActionBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isBindingNotNull(): Boolean = _binding != null

    private fun bindNavController() {
        viewModel.navigationCommands.observe(viewLifecycleOwner) { command ->
            when (command) {
                is NavigationCommand.To -> {
                    command.extras?.let { extras ->
                        navigate(
                            command.directions,
                            extras
                        )
                    } ?: run {
                        findNavController().navigate(
                            command.directions
                        )
                    }
                }
                is NavigationCommand.BackTo -> findNavController().getBackStackEntry(command.destinationId)
                is NavigationCommand.Back -> findNavController().popBackStack()
            }
        }
    }

    private fun bindCommonEffects() {
        viewModel.commonEffect.observe(viewLifecycleOwner) {
            when (it) {
                is NoInternet -> showNoInternet()
                is BackEndError -> showBackEndError()
                is UnknownError -> showBackEndError()
                is MessageError -> showError(it.messageId, it.titleId, it.imageId)
                else -> error("Unknown BaseEffect: $it")
            }
        }
    }

    private fun bindMessage() {
        viewModel.message.observe(viewLifecycleOwner) {
            context?.toast(it)
        }
    }

    open fun navigate(
        @NonNull directions: NavDirections,
        @NonNull navigatorExtras: Navigator.Extras,
    ) {
        findNavController().navigate(directions, navigatorExtras)
    }

    open fun navigate(directions: NavDirections) {
        findNavController().navigate(directions.actionId, directions.arguments)
    }

    protected open fun showNoInternet() {
        if (noInternetDialog?.isAdded == false)
            noInternetDialog?.show("internet-error-dialog")
    }

    protected open fun showBackEndError() {
        BaseBottomSheetDialog.build(action = {
            it.dismiss()
        }).show("general-error-dialog")
    }

    protected open fun showError(
        message: Int,
        title: Int? = R.string.error_unknown_title,
        image: Int? = R.drawable.ic_error,
    ) {
        BaseBottomSheetDialog.build(
            text = message,
            title = title ?: R.string.error_unknown_title,
            image = image ?: R.drawable.ic_error,
            action = {
                it.dismiss()
            }
        ).show("show-error-dialog")
    }

    protected open fun setupActionBar() = with(getHeader()) {
        val color: Int =
            if (isDarkActionBar) R.color.colorPrimary
            else R.color.white

        setupStatusBarColor(activity as MainActivity, color, isDarkActionBar)
        isVisible = isHeaderEnabled

        resetButtons()
        setLeftButton(R.drawable.ic_back) {
            findNavController().popBackStack()
        }

        setupTheme(isDarkActionBar)
    }

    protected fun getHeader(): Header {
        return (activity as MainActivity).getHeader()
    }

    private fun bindLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showLoading()
            else hideLoading()
        }
    }

    protected open fun showLoading() {
        if (loadingDialog?.isAdded == false) loadingDialog?.show("dialog")
    }

    protected open fun hideLoading() {
        if (loadingDialog?.isAdded == true) loadingDialog?.dismiss()
    }

    protected fun <T : DialogFragment> T.show(tag: String? = null): T {
        this.show(this@BaseFragment.parentFragmentManager, tag)
        return this
    }
}