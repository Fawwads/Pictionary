package com.fawwad.pictionary.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fawwad.pictionary.R
import com.fawwad.pictionary.ui.adapters.SelectableAdapter
import com.fawwad.pictionary.ui.adapters.StrokeWidthAdapter
import kotlin.math.min

/**
 * TODO: document your custom view class.
 */
class StrokeWidthListView : RecyclerView, SelectableAdapter.OnSelectionListener {

    val defaultMaxStrokeSize: Int = 20
    val defaultMinStrokeSize: Int = 3
    val defaultStepSize: Int = 2

    var maxStrokeSize = defaultMaxStrokeSize;
    var minStrokeSize = defaultMinStrokeSize;
    var stepSize = defaultStepSize
    var onStrokeWidthSelectListener: StrokeWidthSelectListener? = null
    var strokeAdapter:StrokeWidthAdapter?=null

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
            attrs, R.styleable.StrokeWidthListView, defStyle, 0
        )

        maxStrokeSize =
            a.getInt(R.styleable.StrokeWidthListView_maxStrokeSize, defaultMaxStrokeSize)

        minStrokeSize =
            a.getInt(R.styleable.StrokeWidthListView_minStrokeSize, defaultMinStrokeSize)

        stepSize =
            a.getInt(R.styleable.StrokeWidthListView_stepSize, defaultStepSize)

        a.recycle()

        strokeAdapter = StrokeWidthAdapter(minStrokeSize, maxStrokeSize, stepSize, 0)
        strokeAdapter?.onSelectionListener = this

        setAdapter(strokeAdapter)


    }

    override fun onSelect(position: Int) {
        val width = strokeAdapter?.strokeWidths?.get(position)
        width?.let {
            onStrokeWidthSelectListener?.onStrokeWidthSelected(width)
        }
    }

    interface StrokeWidthSelectListener {
        fun onStrokeWidthSelected(width: Int)
    }


}
