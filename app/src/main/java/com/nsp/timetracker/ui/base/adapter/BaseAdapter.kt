package com.nsp.timetracker.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Entity, H : BaseAdapter.BaseViewHolder<Entity>> :
    RecyclerView.Adapter<H> {
    var items = arrayListOf<Entity>()

    protected var itemClickListener: ItemClickListener<Entity>? = null
    protected var multiClickListener: MultiClickListener<Entity>? = null
    private var emptyView: View? = null
    private var listener: ItemChangeListener? = null
    private var adapterDataObserver: RecyclerView.AdapterDataObserver? = null

    protected val isListEmpty: Boolean
        get() = itemCount == 0

    constructor() {
        init()
    }

    constructor(itemClickListener: ItemClickListener<Entity>) {
        this.itemClickListener = itemClickListener
        init()
    }

    constructor(multiClickListener: MultiClickListener<Entity>) {
        this.multiClickListener = multiClickListener
        init()
    }

    private fun init() {
        adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                checkIfEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                checkIfEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                checkIfEmpty()
            }
        }
        registerAdapterDataObserver(adapterDataObserver as RecyclerView.AdapterDataObserver)
    }

    protected fun checkIfEmpty() {
        if (emptyView != null) {
            emptyView!!.visibility = if (isListEmpty) View.VISIBLE else View.GONE
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapterDataObserver?.let { unregisterAdapterDataObserver(it) }
    }

    /**
     * Listener for item change
     */
    fun setItemChangeListener(listener: ItemChangeListener?) {
        this.listener = listener
    }

    /**
     * Sets the view to show if the adapter is empty.
     */
    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
        checkIfEmpty()
    }

    open fun setItems(items: List<Entity>?) {
        this.items = ArrayList(items)
        if (listener != null) listener!!.onItemChange()
        notifyDataSetChanged()
    }

    open fun addItems(items: List<Entity>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun addItem(item: Entity) {
        items.add(item)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    open fun clear() {
        itemCount
        items.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.bind(items[position], position)
    }

    open class BaseViewHolder<T> : RecyclerView.ViewHolder {
        var item: T? = null
        protected var itemClickListener: ItemClickListener<T>? = null
        protected var multiClickListener: MultiClickListener<T>? = null

        constructor(view: View, itemClickListener: ItemClickListener<T>?) : super(view) {
            this.itemClickListener = itemClickListener
        }

        constructor(view: View, multiClickListener: MultiClickListener<T>?) : super(view) {
            this.multiClickListener = multiClickListener
        }

        open fun bind(item: T, position: Int) {
            this.item = item

            itemClickListener?.let {
                itemView.setOnClickListener { _ ->
                    it.onItemClick(item)
                }
            }
        }
    }

    interface ItemChangeListener {
        fun onItemChange()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}