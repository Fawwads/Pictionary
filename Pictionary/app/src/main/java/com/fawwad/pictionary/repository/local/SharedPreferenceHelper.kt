package com.fawwad.pictionary.repository.local

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.fawwad.pictionary.Pictionary

object SharedPreferenceHelper {

    val sharedPreferences: SharedPreferences? = Pictionary.instance?.getSharedPreferences(
        SharedPreferenceConstants.SHARED_PREFERENCE,
        MODE_PRIVATE
    )

    fun saveUserName(userName: String) {
        sharedPreferences?.edit()?.putString(SharedPreferenceConstants.USER_NAME, userName)?.apply()
    }

    fun getUserName(): String? {
        return sharedPreferences?.getString(SharedPreferenceConstants.USER_NAME, null)
    }

    fun saveSessionId(sessionId: String) {
        sharedPreferences?.edit()?.putString(SharedPreferenceConstants.SESSION_ID, sessionId)
            ?.apply()
    }

    fun getSessionId(): String? {
        return sharedPreferences?.getString(SharedPreferenceConstants.SESSION_ID, null)
    }

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }


}