package com.nsp.timetracker.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.StringRes
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.databinding.ItemCategoryBinding

class HistorySpinnerAdapter(
    context: Context,
    items: List<Category>,
) : ArrayAdapter<Category>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        getItem(position)?.let { category ->
            val itemBinding =
                ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            setItemForCategory(itemBinding, category)
            view = itemBinding.root
        } ?: run {
            setHint(view, R.string.hint_category)
        }

        return view
    }

    private fun setHint(view: View, @StringRes res: Int) {
        view.findViewById<TextView>(android.R.id.text1).setText(res)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getItem(position: Int): Category? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0

    private fun setItemForCategory(binding: ItemCategoryBinding, category: Category) {

    }
}