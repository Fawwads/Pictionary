package com.fawwad.pictionary.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class SelectableAdapter<T> : RecyclerView.Adapter<T>() where T : RecyclerView.ViewHolder {

    var selectedPosition = 0
    var onSelectionListener: OnSelectionListener? = null

    fun selectPosition(position: Int) {
        selectedPosition = position
        onSelectionListener?.onSelect(position)
    }

    interface OnSelectionListener {
        fun onSelect(position: Int)
    }

}