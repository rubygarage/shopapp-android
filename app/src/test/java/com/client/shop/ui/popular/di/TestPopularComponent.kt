package com.client.shop.ui.popular.di

import com.client.shop.ui.popular.PopularFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestPopularModule::class])
interface TestPopularComponent : PopularComponent {

    override fun inject(fragment: PopularFragment)
}