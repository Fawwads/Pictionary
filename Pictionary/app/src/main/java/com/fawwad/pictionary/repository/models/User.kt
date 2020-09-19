package com.fawwad.pictionary.repository.models

data class User(val userName: String = "", var score: Int = 0, val host: Boolean = false, val turn: Boolean = false)