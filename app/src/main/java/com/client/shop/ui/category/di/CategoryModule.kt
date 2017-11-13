package com.client.shop.ui.category.di

import com.client.shop.ui.category.contract.CategoryPresenter
import com.client.shop.ui.category.contract.CategoryUseCase
import dagger.Module
import dagger.Provides

@Module
class CategoryModule {

    @Provides
    fun provideCategoryPresenter(categoryUseCase: CategoryUseCase): CategoryPresenter {
        return CategoryPresenter(categoryUseCase)
    }
}