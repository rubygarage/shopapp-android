package com.client.shop.ui.recent.di

import com.client.shop.ui.recent.RecentActivity
import com.client.shop.ui.recent.RecentFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(RecentModule::class))
interface RecentComponent {

    fun inject(activity: RecentActivity)

    fun inject(fragment: RecentFragment)
}