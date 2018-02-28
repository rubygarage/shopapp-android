package com.shopapp.ui.category.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.category.contract.CategoryPresenter
import dagger.Module
import dagger.Provides

@Module
class TestCategoryModule {

    @Provides
    fun provideCategoryPresenter(): CategoryPresenter = mock()

}