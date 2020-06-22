package com.hje.jan.hencoderplus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val bottom = ObjectAnimator.ofFloat(flipView, "bottomRotation", 0f, 45f)
        val rotate = ObjectAnimator.ofFloat(flipView, "rotationDegree", 0f, 270f)
        val top = ObjectAnimator.ofFloat(flipView, "topRotation", 0f, -45f)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(bottom, rotate, top)
        animatorSet.duration = 2000
        animatorSet.startDelay = 1000
        animatorSet.addListener(onEnd = {
            // animatorSet.start()
        })
        animatorSet.start()*/
    }
}