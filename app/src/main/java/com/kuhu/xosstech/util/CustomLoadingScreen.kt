package com.kuhu.xosstech.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.databinding.DataBindingUtil
import com.kuhu.xosstech.R
import com.kuhu.xosstech.databinding.LayoutCircularLoadingBinding

class CustomLoadingScreen(context: Context) : Dialog(context) {

    private val binding: LayoutCircularLoadingBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_circular_loading,
        null,
        false
    )

    init {
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        startAnimation()
    }

    private fun startAnimation() {
        val outerRotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        outerRotate.duration = 2000
        outerRotate.repeatCount = Animation.INFINITE
        outerRotate.interpolator = LinearInterpolator()

        binding.progressBarClockwise.startAnimation(outerRotate)

        val innerRotate = RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        innerRotate.duration = 2000
        innerRotate.repeatCount = Animation.INFINITE
        innerRotate.interpolator = LinearInterpolator()

        binding.progressBarAntiClockwise.startAnimation(innerRotate)
    }
}