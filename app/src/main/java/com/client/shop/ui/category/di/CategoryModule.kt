package com.client.shop.ui.category.di

import com.client.shop.ui.category.contract.CategoryPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class CategoryModule {

    @Provides
    fun provideBlogPresenter(repository: Repository): CategoryPresenter {
        return CategoryPresenter(repository)
    }
}