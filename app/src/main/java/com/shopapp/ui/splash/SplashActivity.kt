package com.shopapp.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ui.splash.router.SplashRouter
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val BACKGROUND_ANIMATION_DURATION = 1000L
    }

    @Inject
    lateinit var router: SplashRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        ShopApplication.appComponent.attachSplashComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        background.animate()
            .alpha(1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    router.showHome(this@SplashActivity)
                }
            })
            .duration = BACKGROUND_ANIMATION_DURATION
    }
}