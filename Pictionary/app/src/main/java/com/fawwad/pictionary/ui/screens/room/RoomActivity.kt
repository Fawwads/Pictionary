package com.fawwad.pictionary.ui.screens.room

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fawwad.pictionary.R
import com.fawwad.pictionary.repository.local.SharedPreferenceHelper
import com.fawwad.pictionary.ui.adapters.UserAdapter
import com.fawwad.pictionary.ui.screens.BaseActivity
import com.fawwad.pictionary.ui.screens.DrawingActivity
import kotlinx.android.synthetic.main.activity_room.*


class RoomActivity : BaseActivity() {

    lateinit var roomViewModel: RoomViewModel
    lateinit var usersAdapter: UserAdapter

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, RoomActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_room)

        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        roomViewModel.updateSession()

        usersAdapter = UserAdapter()
        usersView.adapter = usersAdapter;
        usersView.layoutManager = LinearLayoutManager(this)

        btnCopyCode.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Code", SharedPreferenceHelper.getSessionId())
            clipboard.setPrimaryClip(clip)
            showMessage("Code copied",1)
        }

        btnShareLink.setOnClickListener {
            showMessage("Coming soon",0)
        }

        btnStart.setOnClickListener {
            DrawingActivity.start(this)
        }


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
            if (it != null) {
                Log.i("SESSION", it.toString())
                usersAdapter.updateUsersList(it.users)

            } else {
                showMessage("Session not found", -1)
                Handler().postDelayed({
                    finish()
                }, 1000)


            }
        })

    }


}
