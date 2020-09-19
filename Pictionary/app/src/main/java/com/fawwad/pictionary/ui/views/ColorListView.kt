package com.fawwad.pictionary.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fawwad.pictionary.R
import com.fawwad.pictionary.ui.adapters.ColorAdapter
import com.fawwad.pictionary.ui.adapters.SelectableAdapter
import com.fawwad.pictionary.ui.adapters.StrokeWidthAdapter

class ColorListView : RecyclerView, SelectableAdapter.OnSelectionListener {

    var colorHeight: Int = 50
    var colorAdapter: ColorAdapter? = null
    var colorSelectListener: ColorSelectListener? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ColorListView, defStyle, 0
        )

        colorHeight =
            a.getInt(R.styleable.ColorListView_colorHeight, 50)


        a.recycle()

        colorAdapter = ColorAdapter(colorHeight, 0)
        colorAdapter?.onSelectionListener = this

        setAdapter(colorAdapter)

    }

    fun setColors(colors:MutableList<Int>){
        colorAdapter?.colors = colors
    }

    override fun onSelect(position: Int) {
        val color = colorAdapter?.colors?.get(position)
        color?.let {
            colorSelectListener?.onColorSelected(color)
        }
    }

    interface ColorSelectListener {
        fun onColorSelected(color: Int)
    }
}