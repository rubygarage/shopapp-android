package com.client.shop.ui.splash.di

import com.client.shop.ui.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SplashModule::class))
interface SplashComponent {

    fun inject(activity: SplashActivity)
}