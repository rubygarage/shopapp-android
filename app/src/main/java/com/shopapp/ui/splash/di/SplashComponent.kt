package com.shopapp.ui.splash.di

import com.shopapp.ui.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    fun inject(activity: SplashActivity)
    
}