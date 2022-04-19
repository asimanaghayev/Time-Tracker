package com.nsp.timetracker.ui.base.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.nsp.timetracker.support.adapter.DateViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SectionedAdapter<ENTITY, HOLDER extends BaseAdapter.BaseViewHolder<ENTITY>, HEADER extends BaseAdapter.BaseViewHolder<Date>>
        extends BaseAdapter {

    private static final int VIEW_TYPE_HEADER = Integer.MAX_VALUE - 1;

    private boolean enableSubHeaders = true;
    private boolean isItemsReversed = false;
    private List<Integer> sectionPositions = new ArrayList<>();

    public SectionedAdapter() {
    }

    public SectionedAdapter(ItemClickListener itemClickListener) {
        super(itemClickListener);
    }

    public SectionedAdapter(MultiClickListener multiClickListener) {
        super(multiClickListener);
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeaderOnPosition(position)) {
            return VIEW_TYPE_HEADER;
        } else {
            return super.getItemViewType(position);
        }
    }

    public int getSectionedViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public BaseAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    public abstract HEADER onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract HOLDER onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract boolean onPlaceSectionBetweenItems(int itemPosition, int nextItemPosition);

    public abstract void onBindHeaderViewHolder(HEADER holder, int position);

    public abstract void onBindItemViewHolder(HOLDER holder, int position);

    @Override
    public int getItemCount() {
        return super.getItemCount() + sectionPositions.size();
    }

    @Override
    public void addItem(Object item) {
        super.addItem(item);
        initSubHeaderPositions();
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List items) {
        super.addItems(items);
        initSubHeaderPositions();
        notifyDataSetChanged();
    }

    @Override
    public void setItems(List items) {
        super.setItems(items);
        initSubHeaderPositions();
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        sectionPositions.clear();
        super.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof DateViewHolder) {
//        if (isHeaderOnPosition(position)) {
            onBindHeaderViewHolder((HEADER) holder, getItemPositionForViewHolder(position));
        } else {
            onBindItemViewHolder((HOLDER) holder, getItemPositionForViewHolder(position));
        }
    }

    public void enableSubHeaders(boolean enable) {
        enableSubHeaders = enable;
    }

    public void setItemReversed(boolean isReversed) {
        this.isItemsReversed = isReversed;
    }

    public int getItemsWithoutHeaderCount() {
        return getItems().size();
    }

    public int getSectionsCount() {
        return sectionPositions.size();
    }

    private int getItemPositionForViewHolder(int viewHolderPosition) {
        return viewHolderPosition - getCountOfSubHeadersBeforeViewPosition(viewHolderPosition);
    }

    protected int getCountOfSubHeadersBeforeViewPosition(int position) {
        int count = 0;
        for (int sectionPosition : sectionPositions) {
            if (sectionPosition < position) {
                count++;
            }
        }
        return count;
    }

    private void initSubHeaderPositions() {
        sectionPositions.clear();

        if (!enableSubHeaders || getItemsWithoutHeaderCount() == 0) {
            return;
        }

        if (!isItemsReversed) {
            sectionPositions.add(0);
        }

        for (int i = 1; i < getItemsWithoutHeaderCount(); i++) {
            if (onPlaceSectionBetweenItems(i - 1, i)) {
                sectionPositions.add(i + sectionPositions.size());
            }
        }

        if (isItemsReversed) {
            sectionPositions.add(getItemsWithoutHeaderCount() + sectionPositions.size());
        }
    }

    protected boolean isHeaderOnPosition(int position) {
        return sectionPositions.contains(position);
    }
}
