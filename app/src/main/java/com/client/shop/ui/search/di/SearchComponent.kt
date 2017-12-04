package com.client.shop.ui.search.di

import com.client.shop.ui.search.SearchActivity
import dagger.Subcomponent

@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    fun inject(activity: SearchActivity)
}