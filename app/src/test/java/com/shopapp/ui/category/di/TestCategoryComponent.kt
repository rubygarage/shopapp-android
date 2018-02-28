package com.shopapp.ui.category.di

import com.shopapp.ui.category.CategoryActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestCategoryModule::class])
interface TestCategoryComponent : CategoryComponent {

    override fun inject(activity: CategoryActivity)

}