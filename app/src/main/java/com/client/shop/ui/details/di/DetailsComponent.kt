package com.client.shop.ui.details.di

import com.client.shop.ui.details.DetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = [DetailsModule::class])
interface DetailsComponent {

    fun inject(activity: DetailsActivity)
}