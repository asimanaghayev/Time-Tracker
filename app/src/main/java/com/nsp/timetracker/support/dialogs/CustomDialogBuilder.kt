package com.nsp.timetracker.support.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.nsp.timetracker.R
import java.util.*

class CustomDialogBuilder private constructor() {
    private lateinit var context: Context
    private var title = ""
    private var message = ""
    private var editValue = ""

    // For Spinner
    private var list: ArrayList<String>? = null

    // For Buttons
    private var confirmationText = ""
    private var cancellationText = ""
    private var showCancelButton = true

    // For EditText input
    private var showEditText = false

    @DrawableRes
    private var resIdIcon = 0
    private var confirmationListener: ConfirmationListener? = null
    private var cancelationListener: CancelationListener? = null
    private var dismissListener: DialogInterface.OnDismissListener? = null
    private var cancelFromOutside = true
    private var isMessageWillClickable = false
    private var startSpanPosition = 0
    private var endSpanPosition = 0
    private var clickableSpan: ClickableSpan? = null

    fun title(title: String): CustomDialogBuilder {
        this.title = title
        return this
    }

    fun title(title: Int): CustomDialogBuilder {
        return title(context.getString(title))
    }

    fun message(message: String): CustomDialogBuilder {
        this.message = message
        return this
    }

    fun message(message: Int): CustomDialogBuilder {
        return message(context.getString(message))
    }

    fun editValue(editValue: String): CustomDialogBuilder {
        this.editValue = editValue
        return this
    }

    fun editValue(editValue: Int): CustomDialogBuilder {
        return editValue(context.getString(editValue))
    }

    fun list(list: ArrayList<String>?): CustomDialogBuilder {
        this.list = list
        return this
    }

    fun list(list: Array<String?>): CustomDialogBuilder {
        this.list = ArrayList(Arrays.asList(*list))
        return this
    }

    fun confirmationText(confirmationText: String): CustomDialogBuilder {
        this.confirmationText = confirmationText
        return this
    }

    fun confirmationText(confirmationText: Int): CustomDialogBuilder {
        return confirmationText(context.getString(confirmationText))
    }

    fun cancellationText(cancelText: String): CustomDialogBuilder {
        cancellationText = cancelText
        return this
    }

    fun cancellationText(cancelText: Int): CustomDialogBuilder {
        return cancellationText(context.getString(cancelText))
    }

    fun icon(@DrawableRes resIdIcon: Int): CustomDialogBuilder {
        this.resIdIcon = resIdIcon
        return this
    }

    fun cancelFromOutside(cancelFromOutside: Boolean): CustomDialogBuilder {
        this.cancelFromOutside = cancelFromOutside
        return this
    }

    fun confirmationListener(listener: ConfirmationListener?): CustomDialogBuilder {
        confirmationListener = listener
        return this
    }

    fun cancelationListener(listener: CancelationListener?): CustomDialogBuilder {
        cancelationListener = listener
        return this
    }

    fun dismissListener(listener: DialogInterface.OnDismissListener?): CustomDialogBuilder {
        dismissListener = listener
        return this
    }

    fun showCancelButton(visible: Boolean): CustomDialogBuilder {
        showCancelButton = visible
        return this
    }

    fun showEditText(visible: Boolean): CustomDialogBuilder {
        showEditText = visible
        return this
    }

    fun setMessageWillClickable(isMessageWillClickable: Boolean): CustomDialogBuilder {
        this.isMessageWillClickable = isMessageWillClickable
        return this
    }

    fun setSpanMinMaxPositionForText(
        startSpanPosition: Int,
        endSpanPosition: Int,
    ): CustomDialogBuilder {
        this.startSpanPosition = startSpanPosition
        this.endSpanPosition = endSpanPosition
        return this
    }

    fun setClicableSpanCallBack(clicableSpanCallBack: ClickableSpan?): CustomDialogBuilder {
        clickableSpan = clicableSpanCallBack
        return this
    }

    fun build(): Dialog {
        return newInstance(context, title, message, editValue,
            list, confirmationText, cancellationText, showEditText,
            resIdIcon, showCancelButton, confirmationListener, cancelationListener,
            dismissListener, cancelFromOutside, isMessageWillClickable, clickableSpan,
            startSpanPosition, endSpanPosition)
    }

    interface ConfirmationListener {
        fun onConfirmed(spinnerItem: String?, editText: String?)
    }

    interface CancelationListener {
        fun onCancel()
    }

    companion object {
        fun newInstance(context: Context): CustomDialogBuilder {
            val builder = CustomDialogBuilder()
            builder.context = context
            builder.cancellationText = context.getString(R.string.btn_cancel)
            builder.confirmationText = context.getString(R.string.btn_ok)
            return builder
        }

        private fun newInstance(
            context: Context,
            title: String,
            message: String,
            editValue: String,
            list: ArrayList<String>?,
            confirmationText: String,
            cancellationText: String,
            showEditText: Boolean,
            resIdIcon: Int,
            showCancelButton: Boolean,
            confirmationlistener: ConfirmationListener?,
            cancelationListener: CancelationListener?,
            dismissListener: DialogInterface.OnDismissListener?,
            cancelFromOutside: Boolean,
            isMessageWillClickable: Boolean,
            clickableSpan: ClickableSpan?,
            startSpanPosition: Int,
            endSpanPosition: Int,
        ): Dialog {
            val dialog: MaterialDialog = MaterialDialog.Builder(context)
                .customView(R.layout.dialog_custom, false)
                .build()
            val icon = dialog.findViewById(R.id.icon) as ImageView
            icon.setImageResource(resIdIcon)
            icon.isVisible = resIdIcon != 0
            dialog.setCanceledOnTouchOutside(cancelFromOutside)
            val titleTextView: TextView = dialog.findViewById(R.id.text) as TextView
            titleTextView.text = title
            titleTextView.isVisible = title.isNotEmpty()
            val messageTextView: TextView = dialog.findViewById(R.id.message) as TextView
            if (!isMessageWillClickable) {
                messageTextView.text = message
            } else {
                makeTextViewClickableAndSetMessage(message,
                    clickableSpan,
                    startSpanPosition,
                    endSpanPosition,
                    messageTextView)
            }
            messageTextView.isVisible = message.isNotEmpty()
            val spinner: AppCompatSpinner = dialog.findViewById(R.id.spinner) as AppCompatSpinner
            if (list != null) {
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, list)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.isVisible = true
            }
            val editText: AppCompatEditText =
                dialog.findViewById(R.id.edit_text) as AppCompatEditText
            editText.isVisible = showEditText
            editText.setText(editValue)
            val okButton: TextView = dialog.findViewById(R.id.ok) as TextView
            okButton.text = confirmationText
            okButton.setOnClickListener {
                var spinnerItem = ""
                if (list != null) {
                    spinnerItem = spinner.selectedItem.toString()
                }
                confirmationlistener?.onConfirmed(spinnerItem, editText.text.toString())
                dialog.dismiss()
            }
            val cancelButton: TextView = dialog.findViewById(R.id.cancel) as TextView
            cancelButton.isVisible = showCancelButton
            cancelButton.text = cancellationText
            cancelButton.setOnClickListener {
                dialog.dismiss()
                cancelationListener?.onCancel()
            }
            if (dismissListener != null) {
                dialog.setOnDismissListener(dismissListener)
            }
            return dialog
        }

        private fun makeTextViewClickableAndSetMessage(
            message: String,
            clickableSpan: ClickableSpan?,
            startSpanPosition: Int,
            endSpanPosition: Int,
            messageTextView: TextView,
        ) {
            val ss = SpannableString(message)
            ss.setSpan(clickableSpan,
                startSpanPosition,
                endSpanPosition,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            messageTextView.text = ss
            messageTextView.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}