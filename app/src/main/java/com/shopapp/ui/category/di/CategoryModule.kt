package com.shopapp.ui.category.di

import com.shopapp.domain.interactor.category.GetCategoriesUseCase
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
    fun provideCategoryListPresenter(getCategoriesUseCase: GetCategoriesUseCase): CategoryListPresenter {
        return CategoryListPresenter(getCategoriesUseCase)
    }

    @Provides
    fun provideCategoryRouter(): CategoryRouter = CategoryRouter()

    @Provides
    fun provideCategoryListRouter(): CategoryListRouter = CategoryListRouter()
}