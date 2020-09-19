package com.fawwad.pictionary.socket

interface PictionaryListener {

    fun onStrokeReceive(x:Float,y:Float)

    fun onStrokeStartedReceive(x:Float,y:Float,color:Int,width:Int)

}