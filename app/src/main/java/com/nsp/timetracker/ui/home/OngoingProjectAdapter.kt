package com.nsp.timetracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.databinding.ItemOngoingProjectBinding
import com.nsp.timetracker.support.listener.EmptyListener
import com.nsp.timetracker.support.util.DateUtils
import com.nsp.timetracker.support.extensions.timeFormat
import com.nsp.timetracker.ui.base.adapter.BaseAdapter
import com.nsp.timetracker.ui.base.adapter.ItemClickListener
import java.util.*

class OngoingProjectAdapter(itemClickListener: ItemClickListener<History>) :
    BaseAdapter<History, BaseAdapter.BaseViewHolder<History>>(itemClickListener) {

    val totalSeconds = MutableLiveData(0L)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<History> {
        return OngoingProjectViewHolder(
            ItemOngoingProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClickListener
        )
    }

    inner class OngoingProjectViewHolder(
        private val binding: ItemOngoingProjectBinding,
        onCLick: ItemClickListener<History>?,
    ) : BaseAdapter.BaseViewHolder<History>(binding.root, onCLick) {

        private val seconds = MutableLiveData(0L)

        init {
            DateUtils.setupTimer(object : EmptyListener {
                override fun onAction() {
                    seconds.postValue(seconds.value?.plus(1))
                }
            })
        }

        override fun bind(item: History, position: Int) {
            super.bind(item, position)
            binding.item = item
            seconds.value = ((Date().time - item.startDate) / 1000)
            totalSeconds.value = (totalSeconds.value?.plus(seconds.value ?: 0))

            binding.btnStop.setOnClickListener {
                itemClickListener!!.onItemClick(item)
            }
            itemView.setOnClickListener {}

            seconds.observeForever {
                setTime(seconds.value ?: 0)
            }
        }

        private fun setTime(seconds: Long) {
            binding.labelTime.text = seconds.timeFormat()
        }
    }
}