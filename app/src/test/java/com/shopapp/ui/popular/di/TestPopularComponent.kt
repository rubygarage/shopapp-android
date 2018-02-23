package com.shopapp.ui.popular.di

import com.shopapp.ui.popular.PopularFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestPopularModule::class])
interface TestPopularComponent : PopularComponent {

    override fun inject(fragment: PopularFragment)
}