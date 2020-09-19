package com.fawwad.pictionary.repository.remote

import android.util.Log
import com.fawwad.pictionary.repository.local.SharedPreferenceHelper
import com.fawwad.pictionary.repository.models.Session
import com.fawwad.pictionary.repository.models.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.Exception

object FirebaseHelper {

    val TAG = "FirebaseHelper"

    val db = Firebase.firestore

    fun createSession(sessionName: String, firebaseListener: FirebaseListener<Boolean>) {
        db.collection(FirebaseContants.SESSIONS).add(
            Session(
                sessionName,
                listOf(User(SharedPreferenceHelper.getUserName()!!, 0, true, false))
            )
        ).addOnCompleteListener {
            if (it.isComplete) {
                Log.i(TAG, "Session created successfully")
                if (it.result != null) {
                    it.result?.let {
                        SharedPreferenceHelper.saveSessionId(it.id)
                        firebaseListener.onResponse(true)
                    }
                } else {
                    firebaseListener.onResponse(false)
                }
            } else {
                it.exception?.let {
                    firebaseListener.onError(it)
                }
            }
        }
    }

    fun getSession(sessionId: String, listener: FirebaseListener<Session>) {
        db.collection(FirebaseContants.SESSIONS).document(sessionId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    listener.onError(e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    val session = snapshot.toObject<Session>()
                    Log.i("FAWWAD", session.toString())
                    session?.let {
                        Log.i("FAWWAD", it.toString())
                        listener.onResponse(session)
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                    listener.onError(Exception("Empty Response"))
                }
            }
    }

    fun joinSession(sessionId: String, name: String, listener: FirebaseListener<Session>) {
        val user = User(name, 0, false, false)
        val sessionRef = db.collection(FirebaseContants.SESSIONS).document(sessionId)
        sessionRef.get().addOnSuccessListener { snapshot ->
            if (snapshot != null && snapshot.exists()) {
                SharedPreferenceHelper.saveSessionId(sessionId)
                Log.d(TAG, "Current data: ${snapshot.data}")
                val session = snapshot.toObject<Session>()
                if (session != null) {
                    if (!session.users.isEmpty()) {
                        val userName = SharedPreferenceHelper.getUserName()
                        for (userObject in session.users) {
                            if (userObject.userName.equals(userName)) {
                                listener.onError(Exception("Name already exists please change name"))
                                return@addOnSuccessListener;
                            }
                        }
                    }
                    addUserInRoom(sessionRef, user, session, listener)
                } else {
                    listener.onError(Exception("Room not found"))
                }
            } else {
                Log.d(TAG, "Current data: null")
                listener.onError(Exception("Room not found"))
            }
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Listen failed.", e)
                listener.onError(e)
            }


    }

    private fun addUserInRoom(
        sessionRef: DocumentReference,
        user: User,
        session: Session,
        listener: FirebaseListener<Session>
    ) {
        sessionRef.update("users", FieldValue.arrayUnion(user))
            .addOnSuccessListener { listener.onResponse(session) }
            .addOnFailureListener { e -> listener.onError(Exception("Unable to join room, Please try again")) }
    }


    interface FirebaseListener<T> {
        fun onResponse(t: T)
        fun onError(e: Exception)
    }


}

