package com.fawwad.pictionary.ui.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fawwad.pictionary.R
import com.fawwad.pictionary.repository.local.SharedPreferenceHelper
import com.fawwad.pictionary.socket.PictionaryListener
import com.fawwad.pictionary.socket.PictionarySocket
import com.fawwad.pictionary.ui.screens.room.RoomViewModel
import com.fawwad.pictionary.ui.views.ColorListView
import com.fawwad.pictionary.ui.views.DrawingView
import com.fawwad.pictionary.ui.views.StrokeWidthListView
import kotlinx.android.synthetic.main.activity_drawing.*
import net.cachapa.expandablelayout.ExpandableLayout



class DrawingActivity : BaseActivity(), StrokeWidthListView.StrokeWidthSelectListener,
    ColorListView.ColorSelectListener, DrawingView.OnStrokeListener, PictionaryListener {

    lateinit var roomViewModel: RoomViewModel

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DrawingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)

        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        roomViewModel.updateSession()
        roomViewModel.getSessionLiveData().observe(this, Observer {
            if (it != null) {
                Log.i("SESSION", it.toString())
            } else {
                showMessage("Session not found", -1)
                Handler().postDelayed({
                    finish()
                }, 1000)
            }
        })



        initViews()

        initSocket()

    }

    private fun initSocket() {
        PictionarySocket.connect()
        PictionarySocket.attachListener(this)
        val sessionId =  SharedPreferenceHelper.getSessionId()
        if (!TextUtils.isEmpty(sessionId)){
            PictionarySocket.createRoom(sessionId!!)
        } else {
            showMessage("Session not found", -1)
            Handler().postDelayed({
                finish()
            }, 1000)
        }
    }

    fun initViews(){
        toggleBrush.setOnClickListener {
            expandableStrokeView.toggle()
        }
        toggleColor.setOnClickListener {
            expandableColorView.toggle()
        }
        erase.setOnClickListener {
            drawingView.clear()
        }
        toggleControls.setOnClickListener {
            expandableControls.toggle()
        }

        expandableControls.setOnExpansionUpdateListener { expansionFraction, state ->
            when (state) {
                ExpandableLayout.State.COLLAPSED -> {
                    expandableColorView.collapse()
                    expandableStrokeView.collapse()
                    toggleControls.setImageResource(R.drawable.ic_expand_more)
                }
                ExpandableLayout.State.EXPANDED -> {
                    toggleControls.setImageResource(R.drawable.ic_expand_less)
                }
            }
        }

        strokeView.onStrokeWidthSelectListener = this
        val mutableColors = mutableListOf<Int>()
        val colors = resources.getIntArray(R.array.colors)
        for (color in colors) mutableColors.add(color)
        colorView.setColors(mutableColors)
        colorView.colorSelectListener = this

        drawingView.color = mutableColors[0]
        drawingView.strokeWidth = strokeView.minStrokeSize.toFloat()

        drawingView.onStrokeListener = this
    }


    override fun onStrokeWidthSelected(width: Int) {
        drawingView.strokeWidth = width.toFloat()
    }

    override fun onColorSelected(color: Int) {
        drawingView.color = color
    }

    override fun onStrokeStarted(
        x: Float,
        y: Float,
        color: Int,
        strokeWidth: Float
    ) {
       PictionarySocket.onStrokeStarted(x,y,color,strokeWidth.toInt())
    }

    override fun onStroke(x: Float, y: Float) {
        PictionarySocket.onStroke(x,y)
    }

    override fun onStrokeFinished() {

    }


    override fun onStrokeReceive(x: Float, y: Float) {
        runOnUiThread{
            drawingView.onMoveEvent(x,y)
            drawingView.invalidate()
        }
    }

    override fun onStrokeStartedReceive(x: Float, y: Float, color: Int, width: Int) {
        runOnUiThread {
            drawingView.onDownEvent(x,y,color,width.toFloat())
            drawingView.invalidate()
        }
    }


}
