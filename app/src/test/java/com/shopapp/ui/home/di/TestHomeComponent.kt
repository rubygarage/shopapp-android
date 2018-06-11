package com.shopapp.ui.home.di

import com.shopapp.ui.home.HomeFragment
import dagger.Subcomponent

@Subcomponent()
interface TestHomeComponent : HomeComponent {

    override fun inject(fragment: HomeFragment)
}