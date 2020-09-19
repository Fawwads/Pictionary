package com.fawwad.pictionary.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.util.Pair
import com.fawwad.pictionary.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            var pairBackground = Pair.create(backgroundView as View, backgroundView.transitionName)
            var pairText = Pair.create(titleView as View, titleView.transitionName)
            var pairGradient = Pair.create(gradientView, gradientView.transitionName)
            HomeActivity.start(this, pairBackground, pairGradient, pairText)
//            Handler().postDelayed({
//                finishAffinity()
//            },1000)

        }, 1000)

    }
}
