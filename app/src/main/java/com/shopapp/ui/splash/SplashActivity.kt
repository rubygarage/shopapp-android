package com.shopapp.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.splash.contract.SplashPresenter
import com.shopapp.ui.splash.contract.SplashView
import com.shopapp.ui.splash.router.SplashRouter
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseLceActivity<Unit, SplashView, SplashPresenter>(), SplashView {

    companion object {
        private const val BACKGROUND_ANIMATION_DURATION = 1000L
    }

    @Inject
    lateinit var router: SplashRouter

    @Inject
    lateinit var splashPresenter: SplashPresenter

    private var isRoutingAllowed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        background.animate()
            .alpha(1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    changeRoutingState()
                }
            })
            .duration = BACKGROUND_ANIMATION_DURATION

        loadData(true)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachSplashComponent().inject(this)
    }

    override fun getMainLayout() = R.layout.activity_lce_without_toolbar

    override fun getContentView() = R.layout.activity_splash

    override fun createPresenter() = splashPresenter

    private fun changeRoutingState() {
        if (isRoutingAllowed) {
            router.showHome(this)
        } else {
            isRoutingAllowed = true
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.validateItems()
    }

    override fun showContent(data: Unit) {
        super.showContent(data)
        changeRoutingState()
    }

    override fun validationError() {
        changeRoutingState()
    }
}