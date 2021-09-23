package com.example.ExoPlayerApp.base

import android.content.Context
import android.widget.BaseAdapter

abstract class BaseAdapter<T>(
	private val context: Context,
    private val dataSource: List<T>
) : BaseAdapter() {
	private var selectedPosition = -1

	fun selectedPosition(position: Int) {
		selectedPosition = position
		notifyDataSetChanged()
	}

	override fun getItem(position: Int): T = dataSource[position]

	override fun getItemId(position: Int): Long = position.toLong()

	override fun getCount(): Int = dataSource.size

	fun getDataSource() = dataSource
}
