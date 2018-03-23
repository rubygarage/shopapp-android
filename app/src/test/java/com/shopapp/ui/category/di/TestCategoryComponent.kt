package com.shopapp.ui.category.di

import com.shopapp.ui.category.CategoryActivity
import com.shopapp.ui.category.CategoryListFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestCategoryModule::class])
interface TestCategoryComponent : CategoryComponent {

    override fun inject(activity: CategoryActivity)

    override fun inject(activity: CategoryListFragment)

}