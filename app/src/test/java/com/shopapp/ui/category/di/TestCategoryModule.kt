package com.shopapp.ui.category.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.category.contract.CategoryListPresenter
import com.shopapp.ui.category.contract.CategoryPresenter
import com.shopapp.ui.category.router.CategoryListRouter
import com.shopapp.ui.category.router.CategoryRouter
import dagger.Module
import dagger.Provides

@Module
class TestCategoryModule {

    @Provides
    fun provideCategoryPresenter(): CategoryPresenter = mock()

    @Provides
    fun provideCategoryListPresenter(): CategoryListPresenter = mock()

    @Provides
    fun provideCategoryRouter(): CategoryRouter = mock()

    @Provides
    fun provideCategoryListRouter(): CategoryListRouter = mock()

}