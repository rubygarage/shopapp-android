package com.client.shop.ui.search.di

import com.client.shop.ui.search.CategoryListFragment
import com.client.shop.ui.search.SearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    fun inject(activity: SearchFragment)

    fun inject(activity: CategoryListFragment)
}