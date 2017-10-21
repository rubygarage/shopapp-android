package com.client.shop.ui.category.di

import com.client.shop.ui.category.CategoryFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(CategoryModule::class))
interface CategoryComponent {

    fun inject(fragment: CategoryFragment)
}