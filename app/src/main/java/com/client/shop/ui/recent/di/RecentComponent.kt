package com.client.shop.ui.recent.di

import com.client.shop.ui.recent.RecentFragment
import dagger.Subcomponent

@Subcomponent(modules = [RecentModule::class])
interface RecentComponent {

    fun inject(fragment: RecentFragment)
}