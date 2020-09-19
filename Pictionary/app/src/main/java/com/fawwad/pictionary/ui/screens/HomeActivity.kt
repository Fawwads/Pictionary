package com.fawwad.pictionary.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fawwad.pictionary.R
import com.fawwad.pictionary.repository.local.SharedPreferenceHelper
import com.fawwad.pictionary.repository.models.Session
import com.fawwad.pictionary.repository.remote.FirebaseHelper
import com.fawwad.pictionary.ui.screens.room.RoomActivity
import com.fawwad.pictionary.ui.screens.room.RoomViewModel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    lateinit var roomViewModel: RoomViewModel


    companion object {
        fun start(context: Context, vararg pairs: Pair<View, String>) {
            val activityOptionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, *pairs)
            val intent = Intent(
                context,
                HomeActivity::class.java
            )
            context.startActivity(intent, activityOptionsCompat.toBundle())

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_home)
        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        roomViewModel.getProgressLiveData().observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })



        roomViewModel.getErrorLiveData().observe(this, Observer {
            it?.let {
                it.message?.let {
                    showMessage(it, -1)
                }

            }
        })

        roomViewModel.getSessionLiveData().observe(this, Observer {
            it?.let {
                RoomActivity.start(this)
            }
        })

        window.sharedElementEnterTransition.duration = 800

        createRoomView.setOnClickListener {
            if (saveName()) {
                roomViewModel.createSession(etName.text.trim().toString()+"::"+System.currentTimeMillis())
            }
        }

        joinRoomView.setOnClickListener {
            if (saveName() && saveCode()) {
                roomViewModel.joinSession(etCode.text.trim().toString(),etName.text.trim().toString())
            }
        }

        val name = SharedPreferenceHelper.getUserName();
        name?.let {
            etName.setText(it)
        }

        val sessionId = SharedPreferenceHelper.getSessionId()
        if (!TextUtils.isEmpty(sessionId)) {
            RoomActivity.start(this)
        }

    }

    fun saveName(): Boolean {
        if (etName.text.trim().toString().isEmpty()) {
            etName.setError("Please enter name")
            return false
        } else {
            SharedPreferenceHelper.saveUserName(etName.text.trim().toString())
        }
        return true
    }

    fun saveCode(): Boolean {
        if (etCode.text.trim().toString().isEmpty()) {
            etCode.setError("Please enter code")
            return false
        }
        return true
    }

    override fun onBackPressed() {
        finish()
    }
}
