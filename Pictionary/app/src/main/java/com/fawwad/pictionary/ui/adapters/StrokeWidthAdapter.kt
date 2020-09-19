package com.fawwad.pictionary.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fawwad.pictionary.R

class StrokeWidthAdapter : SelectableAdapter<StrokeWidthAdapter.StrokeWidthViewHolder> {

    val minStrokeSize: Int
    val maxStrokeSize: Int
    val stepSize: Int
    val strokeWidths = mutableListOf<Int>()


    constructor(minStrokeSize: Int, maxStrokeSize: Int, stepSize: Int, selectedStrokePosition: Int) {
        this.maxStrokeSize = maxStrokeSize
        this.minStrokeSize = minStrokeSize
        this.stepSize = stepSize
        for (i in minStrokeSize..maxStrokeSize step stepSize) strokeWidths.add(i)
        this.selectedPosition = selectedStrokePosition
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrokeWidthViewHolder {
        return StrokeWidthViewHolder(
            View.inflate(
                parent.context,
                R.layout.row_stroke_item, null
            )
        )
    }

    override fun getItemCount(): Int {
        return strokeWidths.size
    }

    override fun onBindViewHolder(holder: StrokeWidthViewHolder, position: Int) {
        holder.bind(strokeWidths[position], position)
    }

    inner class StrokeWidthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var foregroundView: View? = null
        private var backgroundView: View? = null

        init {
            foregroundView = itemView.findViewById(R.id.view_stroke_width_foreground)
            backgroundView = itemView.findViewById(R.id.view_stroke_width_background)
        }

        fun bind(size: Int, position: Int) {
            // backgroundView?.backgroundTintList = ColorStateList.valueOf(color)
            //   foregroundView?.backgroundTintList = ColorStateList.valueOf(color)

            backgroundView?.layoutParams?.apply {
                width = (maxStrokeSize * 1.5).toInt()
                height = (maxStrokeSize * 1.5).toInt()
            }
            foregroundView?.layoutParams?.apply {
                width = size
                height = size
            }
            if (selectedPosition == position) {
                backgroundView?.setBackgroundResource(R.drawable.stroke_width_background)
            } else {
                backgroundView?.setBackgroundResource(android.R.color.transparent)
            }
            backgroundView?.setOnClickListener {
                selectPosition(position)
                notifyDataSetChanged()
            }
        }

    }
}