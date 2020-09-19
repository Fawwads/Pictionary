package com.fawwad.pictionary.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * TODO: document your custom view class.
 */
class DrawingView : View {

    val strokes: MutableList<Stroke> = mutableListOf()
    lateinit var paint: Paint

    val defaultStrokeWidth = 3f
    val defaultColor = Color.BLACK

    var color: Int = defaultColor

    var strokeWidth: Float = defaultStrokeWidth

    var onStrokeListener: OnStrokeListener? = null


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

        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = defaultStrokeWidth
            color = defaultColor
        }

        val a = context.obtainStyledAttributes(
            attrs, com.fawwad.pictionary.R.styleable.DrawingView, defStyle, 0
        )
        color = a.getColor(com.fawwad.pictionary.R.styleable.DrawingView_drawingColor, defaultColor)
        strokeWidth = a.getFloat(
            com.fawwad.pictionary.R.styleable.DrawingView_penWidth,
            defaultStrokeWidth
        )

        a.recycle()
        // Update TextPaint and text measurements from attributes
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (stroke in strokes) {
            paint.color = stroke.color
            paint.strokeWidth = stroke.strokeWidth
            canvas.drawPath(stroke.path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                onDownEvent(event.getX(), event.getY(), color, strokeWidth)
                onStrokeListener?.onStrokeStarted(event.getX(), event.getY(), color, strokeWidth)
            }
            MotionEvent.ACTION_MOVE -> {
                onMoveEvent(event.getX(),event.getY())
                onStrokeListener?.onStroke(event.getX(),event.getY())
            }
            MotionEvent.ACTION_UP -> {
                onStrokeListener?.onStrokeFinished()
            }
        }
        invalidate()
        return true
    }

    fun onDownEvent(x:Float,y:Float,color:Int,strokeWidth: Float){
        addNewStroke(x, y, color, strokeWidth)
    }

    fun onMoveEvent(x:Float,y:Float){
        val stroke = strokes[strokes.size - 1]
        stroke.path.lineTo(x, y);
    }

    fun addNewStroke(x: Float, y: Float, color: Int, width: Float) {
        val path = Path()
        path.moveTo(x, y)
        strokes += Stroke(path, color, width)
    }

    fun clear(){
        strokes.removeAll { true }
        invalidate()
    }


    interface OnStrokeListener {
        fun onStrokeStarted(
            x: Float,
            y: Float,
            color: Int,
            strokeWidth: Float
        )
        fun onStroke(x: Float, y: Float)
        fun onStrokeFinished()
    }

}
