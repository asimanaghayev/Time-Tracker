package com.nsp.timetracker.support.watcher

import android.text.Editable
import android.text.TextWatcher

open class EmptyTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // implemented empty for to be used as overridden
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // implemented empty for to be used as overridden
    }

    override fun afterTextChanged(s: Editable) {
        // implemented empty for to be used as overridden
    }
}