package com.kuhu.xosstech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kuhu.xosstech.MainActivity
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.ActivitySplashBinding
import com.kuhu.xosstech.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        startShakyAnimation()

        // Check if token exists after a delay
        Handler().postDelayed({
            if (viewModel.checkToken()) {
                // Token exists, navigate to the main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                // Token doesn't exist, navigate to another activity
                val intent = Intent(this, StoryBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            }
        }, 4000) // Adjust the delay as needed
    }

    private fun startShakyAnimation() {
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 1000 // Set the duration of each fade in and fade out cycle
        alphaAnimation.repeatCount = Animation.INFINITE
        alphaAnimation.repeatMode = Animation.REVERSE
        imvSplash.startAnimation(alphaAnimation)
    }
}