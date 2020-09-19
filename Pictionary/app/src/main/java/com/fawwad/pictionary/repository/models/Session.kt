package com.fawwad.pictionary.repository.models

data class Session(val sessionName: String = "", val users: List<User> = listOf())