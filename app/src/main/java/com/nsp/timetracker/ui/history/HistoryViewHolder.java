package com.nsp.timetracker.ui.history;

import android.view.View;

import com.nsp.timetracker.data.db.model.History;
import com.nsp.timetracker.databinding.ItemHistoryBinding;
import com.nsp.timetracker.ui.base.adapter.BaseAdapter;
import com.nsp.timetracker.ui.base.adapter.MultiClickListener;

public class HistoryViewHolder extends BaseAdapter.BaseViewHolder<History> {
    private ItemHistoryBinding binding;

    HistoryViewHolder(View view, MultiClickListener<History> multiClickListener) {
        super(view, multiClickListener);
        binding = ItemHistoryBinding.bind(itemView);
    }

    @Override
    public void bind(History history, int position) {
        super.bind(history, position);
        binding.setItem(history);
    }
}