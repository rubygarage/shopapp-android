package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.contract.BlogPresenter
import com.domain.interactor.blog.ArticleListUseCase
import dagger.Module
import dagger.Provides

@Module
class BlogModule {

    @Provides
    fun provideBlogPresenter(articleListUseCase: ArticleListUseCase): BlogPresenter {
        return BlogPresenter(articleListUseCase)
    }
}