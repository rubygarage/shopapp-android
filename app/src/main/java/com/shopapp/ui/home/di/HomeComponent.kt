package com.shopapp.ui.home.di

import com.shopapp.ui.home.HomeFragment
import dagger.Subcomponent

@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {

    fun inject(fragment: HomeFragment)
}