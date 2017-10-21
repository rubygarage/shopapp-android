package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.contract.BlogPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class BlogModule {

    @Provides
    fun provideBlogPresenter(repository: Repository): BlogPresenter {
        return BlogPresenter(repository)
    }
}