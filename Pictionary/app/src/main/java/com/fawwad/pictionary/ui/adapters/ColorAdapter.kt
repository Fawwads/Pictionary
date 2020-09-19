package com.fawwad.pictionary.ui.adapters


import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fawwad.pictionary.R

class ColorAdapter : SelectableAdapter<ColorAdapter.ColorViewHolder> {

    var colors = mutableListOf<Int>()
    val colorSize: Int

    constructor(colorSize: Int, selectedColor: Int) {
        this.colorSize = colorSize
        this.selectedPosition = selectedColor
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            View.inflate(
                parent.context,
                R.layout.row_stroke_item, null
            )
        )
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], position)
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var foregroundView: View? = null
        private var backgroundView: View? = null

        init {
            foregroundView = itemView.findViewById(R.id.view_stroke_width_foreground)
            backgroundView = itemView.findViewById(R.id.view_stroke_width_background)
        }

        fun bind(color: Int, position: Int) {

            foregroundView?.backgroundTintList = ColorStateList.valueOf(color)

            backgroundView?.layoutParams?.apply {
                width = (colorSize * 1.5).toInt()
                height = (colorSize * 1.5).toInt()
            }
            foregroundView?.layoutParams?.apply {
                width = colorSize
                height = colorSize
            }
            if (selectedPosition == position) {
                backgroundView?.setBackgroundResource(R.drawable.stroke_width_background)
                backgroundView?.backgroundTintList=(ColorStateList.valueOf((color)))
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