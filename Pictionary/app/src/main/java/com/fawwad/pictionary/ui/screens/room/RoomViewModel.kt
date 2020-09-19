package com.fawwad.pictionary.ui.screens.room

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fawwad.pictionary.repository.local.SharedPreferenceHelper
import com.fawwad.pictionary.repository.remote.FirebaseHelper
import com.fawwad.pictionary.repository.models.Session
import com.fawwad.pictionary.repository.remote.FirebaseHelper.FirebaseListener

class RoomViewModel : ViewModel() {

    private var currentSessionLiveData: MutableLiveData<Session> = MutableLiveData()

    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var showErrorLiveData: MutableLiveData<java.lang.Exception> = MutableLiveData()

    fun getSessionLiveData(): LiveData<Session> {
        return currentSessionLiveData
    }

    fun getProgressLiveData(): LiveData<Boolean> {
        return progressLiveData
    }

    fun getErrorLiveData(): LiveData<java.lang.Exception> {
        return showErrorLiveData
    }


    fun createSession(sessionName: String) {
        progressLiveData.postValue(true)
        FirebaseHelper.createSession(sessionName, object : FirebaseListener<Boolean> {
            override fun onResponse(t: Boolean) {
                if (t) {
                    currentSessionLiveData.postValue(Session(sessionName))
                } else {
                    showErrorLiveData.postValue(java.lang.Exception("Session not created"))
                }
                progressLiveData.postValue(false)
            }

            override fun onError(e: Exception) {
                showErrorLiveData.postValue(e)
                progressLiveData.postValue(false)
            }

        })
    }

    fun updateSession() {
        val sessionId = SharedPreferenceHelper.getSessionId()
        if (!TextUtils.isEmpty(sessionId)) {
            progressLiveData.postValue(true)
            FirebaseHelper.getSession(sessionId!!, object : FirebaseListener<Session> {

                override fun onResponse(t: Session) {
                    currentSessionLiveData.value = t
                    progressLiveData.postValue(false)
                }

                override fun onError(e: Exception) {
                    showErrorLiveData.postValue(e)
                    progressLiveData.postValue(false)
                }


            })
        } else {
            currentSessionLiveData.postValue(null)
        }
    }

    fun joinSession(sessionId: String, userName: String) {
        progressLiveData.postValue(true)
        FirebaseHelper.joinSession(sessionId, userName, object : FirebaseListener<Session> {

            override fun onResponse(t: Session) {
                currentSessionLiveData.value = t
                progressLiveData.postValue(false)
            }

            override fun onError(e: Exception) {
                showErrorLiveData.postValue(e)
                progressLiveData.postValue(false)
            }

        })
    }


}