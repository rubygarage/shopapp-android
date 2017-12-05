package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.contract.ArticlePresenter
import com.client.shop.ui.blog.contract.BlogPresenter
import com.domain.interactor.blog.ArticleListUseCase
import com.domain.interactor.blog.ArticleUseCase
import dagger.Module
import dagger.Provides

@Module
class BlogModule {

    @Provides
    fun provideBlogPresenter(articleListUseCase: ArticleListUseCase): BlogPresenter {
        return BlogPresenter(articleListUseCase)
    }

    @Provides
    fun provideArticlePresenter(articleUseCase: ArticleUseCase): ArticlePresenter {
        return ArticlePresenter(articleUseCase)
    }
}