package com.nsp.timetracker.ui.base.adapter

interface MultiClickListener<T> {
    fun onItemClick(item: T, position: Int, clickType: Int)
}