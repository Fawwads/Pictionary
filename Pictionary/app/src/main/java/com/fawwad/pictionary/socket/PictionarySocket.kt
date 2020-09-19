package com.fawwad.pictionary.socket

import android.R
import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


object PictionarySocket {

    val TAG = "PictionarySocket"

    var roomId: String? = null

    var socket = IO.socket("http://192.168.0.104:3000")

    var pictionaryListener: PictionaryListener? = null

    private val onStrokeStartedListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        Log.i(TAG, data.toString())
        val x = data.optDouble("x")
        val y = data.optDouble("y")
        val color = data.optInt("color")
        val width = data.optInt("width")
        pictionaryListener?.onStrokeStartedReceive(x.toFloat(), y.toFloat(), color, width)
    }
    private val onStrokeListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        Log.i(TAG, data.toString())
        val x = data.optDouble("x")
        val y = data.optDouble("y")
        pictionaryListener?.onStrokeReceive(x.toFloat(), y.toFloat())
    }

    fun connect() {

        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        socket.connect()
    }

    fun attachListener(pictionaryListener: PictionaryListener) {
        this.pictionaryListener = pictionaryListener
        socket.on("onStrokeStarted", onStrokeStartedListener);
        socket.on("onStroke", onStrokeListener);
    }

    fun createRoom(roomId: String) {
        this.roomId = roomId
        val data = JSONObject()
        data.put("id", roomId)
        socket.emit("create_session", data)
    }

    fun joinRoom(roomId: String) {
        this.roomId = roomId
        val data = JSONObject()
        data.put("id", roomId)
        socket.emit("create_session", data)
    }

    fun onStrokeStarted(x: Float, y: Float, color: Int, width: Int) {
        val data = JSONObject()
        data.put("id", roomId)
        data.put("x", x)
        data.put("y", y)
        data.put("color", color)
        data.put("width", width)
        socket.emit("onStrokeStarted", data)
    }

    fun onStroke(x: Float, y: Float) {
        val data = JSONObject()
        data.put("id", roomId)
        data.put("x", x)
        data.put("y", y)
        socket.emit("onStroke", data)
    }

    fun disconnect() {
        socket.off("onStrokeStarted", onStrokeStartedListener);
        socket.off("onStroke", onStrokeListener);
        socket.disconnect()
    }


    private val onConnect = Emitter.Listener {
        Log.i(TAG, "Connected")
    }

    private val onDisconnect = Emitter.Listener {
        Log.i(TAG, "diconnected")
    }

    private val onConnectError = Emitter.Listener { args ->
        Log.i(TAG, "Error connecting " + args[0].toString())
    }

}