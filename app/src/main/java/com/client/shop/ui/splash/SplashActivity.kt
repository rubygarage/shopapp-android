package com.client.shop.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.BaseMvpActivity
import com.client.shop.ui.home.HomeActivity
import com.client.shop.ui.splash.contract.SplashPresenter
import com.client.shop.ui.splash.contract.SplashView
import com.client.shop.ui.splash.di.SplashModule
import com.domain.entity.Category
import com.domain.entity.Shop
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseMvpActivity<SplashView, SplashPresenter, SplashViewState>(), SplashView {

    @Inject lateinit var splashPresenter: SplashPresenter

    companion object {
        private const val BACKGROUND_ANIMATION_DURATION = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        background.animate()
                .alpha(1f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        presenter.requestShop()
                    }
                })
                .duration = BACKGROUND_ANIMATION_DURATION
    }

    override fun inject(component: AppComponent) {
        component.attachSplashComponent(SplashModule()).inject(this)
    }

    override fun createPresenter() = splashPresenter

    override fun onNewViewStateInstance() {

    }

    override fun createViewState() = SplashViewState()

    override fun dataReceived(shop: Shop, categories: List<Category>) {
        startActivity(HomeActivity.getStartIntent(this, shop, categories))
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showMessage(message: String, isError: Boolean) {
        if (isError) {

        }
    }
}