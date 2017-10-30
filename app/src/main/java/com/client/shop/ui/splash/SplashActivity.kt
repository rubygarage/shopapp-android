package com.client.shop.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.client.shop.R
import com.client.shop.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val BACKGROUND_ANIMATION_DURATION = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        background.animate()
                .alpha(1f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        startActivity(HomeActivity.getStartIntent(this@SplashActivity))
                    }
                })
                .duration = BACKGROUND_ANIMATION_DURATION
    }
}