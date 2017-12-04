package com.client.shop.ui.home.di

import com.client.shop.ui.home.HomeActivity
import dagger.Subcomponent

@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {

    fun inject(activity: HomeActivity)
}