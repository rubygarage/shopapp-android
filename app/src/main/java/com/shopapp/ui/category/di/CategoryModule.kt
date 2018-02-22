package com.shopapp.ui.category.di

import com.shopapp.domain.interactor.category.CategoryUseCase
import com.shopapp.ui.category.contract.CategoryPresenter
import dagger.Module
import dagger.Provides

@Module
class CategoryModule {

    @Provides
    fun provideCategoryPresenter(categoryUseCase: CategoryUseCase): CategoryPresenter {
        return CategoryPresenter(categoryUseCase)
    }
}