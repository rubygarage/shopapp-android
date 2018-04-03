package com.shopapp.ui.splash.di

import com.shopapp.ui.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestSplashModule::class])
interface TestSplashComponent : SplashComponent {

    override fun inject(activity: SplashActivity)

}