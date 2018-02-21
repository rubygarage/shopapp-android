package com.client.shop.ui.category.di

import com.client.shop.ui.category.CategoryActivity
import dagger.Subcomponent

@Subcomponent(modules = [CategoryModule::class])
interface CategoryComponent {

    fun inject(activity: CategoryActivity)
}