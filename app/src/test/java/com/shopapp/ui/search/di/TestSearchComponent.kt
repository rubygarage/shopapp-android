package com.shopapp.ui.search.di

import com.shopapp.ui.search.SearchFragment
import com.shopapp.ui.search.SearchWithCategoriesFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestSearchModule::class])
interface TestSearchComponent : SearchComponent {

    override fun inject(activity: SearchFragment)

    override fun inject(fragment: SearchWithCategoriesFragment)

}