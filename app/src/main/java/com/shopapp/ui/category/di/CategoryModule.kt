package com.shopapp.ui.category.di

import com.shopapp.domain.interactor.category.CategoryListUseCase
import com.shopapp.domain.interactor.category.CategoryUseCase
import com.shopapp.ui.category.contract.CategoryListPresenter
import com.shopapp.ui.category.contract.CategoryPresenter
import com.shopapp.ui.category.router.CategoryListRouter
import com.shopapp.ui.category.router.CategoryRouter
import dagger.Module
import dagger.Provides

@Module
class CategoryModule {

    @Provides
    fun provideCategoryPresenter(categoryUseCase: CategoryUseCase): CategoryPresenter {
        return CategoryPresenter(categoryUseCase)
    }

    @Provides
    fun provideCategoryListPresenter(categoryListUseCase: CategoryListUseCase): CategoryListPresenter {
        return CategoryListPresenter(categoryListUseCase)
    }

    @Provides
    fun provideCategoryRouter(): CategoryRouter = CategoryRouter()

    @Provides
    fun provideCategoryListRouter(): CategoryListRouter = CategoryListRouter()
}