package com.shopapp.ui.category.di

import com.shopapp.ui.category.CategoryActivity
import com.shopapp.ui.category.CategoryListFragment
import dagger.Subcomponent

@Subcomponent(modules = [CategoryModule::class])
interface CategoryComponent {

    fun inject(activity: CategoryActivity)

    fun inject(activity: CategoryListFragment)
}