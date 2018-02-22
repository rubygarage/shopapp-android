package com.shopapp.ui.popular.di

import com.shopapp.ui.popular.PopularFragment
import dagger.Subcomponent

@Subcomponent(modules = [PopularModule::class])
interface PopularComponent {

    fun inject(fragment: PopularFragment)
}