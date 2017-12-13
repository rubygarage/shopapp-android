package com.client.shop.ui.popular.di

import com.client.shop.ui.popular.PopularFragment
import dagger.Subcomponent

@Subcomponent(modules = [PopularModule::class])
interface PopularComponent {

    fun inject(fragment: PopularFragment)
}