package com.fawwad.pictionary.ui.screens

import android.R.id
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fawwad.pictionary.Pictionary
import com.fawwad.pictionary.R
import com.fawwad.pictionary.repository.local.SharedPreferenceHelper
import com.fawwad.pictionary.socket.PictionarySocket
import com.fawwad.pictionary.ui.dialogs.ConfirmationDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun addContentView(resourceId: Int) {
        View.inflate(this, resourceId, contentView)
    }

    fun showProgress() {
        progressView.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressView.visibility = View.GONE
    }

    fun showMessage(message: String, type: Int) {
        runOnUiThread {
            val snackbar = Snackbar.make(contentView, message, Snackbar.LENGTH_SHORT)
            (snackbar.view
                .findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView)
                .maxLines = 5
            if (type > 0) snackbar.view
                .setBackgroundResource(R.color.success)
            else if (type < 0) snackbar.view
                .setBackgroundResource(R.color.error)
            snackbar.show()
        }
    }

    override fun onBackPressed() {
        val sessionId = SharedPreferenceHelper.getSessionId()
        if (!TextUtils.isEmpty(sessionId)){
            ConfirmationDialog(this)
                .setMessage("Are you sure you want to leave this session?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    SharedPreferenceHelper.saveSessionId("")
                    dialog.dismiss()
                    PictionarySocket.disconnect()
                    super.onBackPressed()
                 })
                .setNegativeButton("Cancel",null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}
