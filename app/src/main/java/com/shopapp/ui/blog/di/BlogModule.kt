package com.shopapp.ui.blog.di

import com.shopapp.domain.interactor.blog.ArticleListUseCase
import com.shopapp.domain.interactor.blog.ArticleUseCase
import com.shopapp.ui.blog.contract.ArticlePresenter
import com.shopapp.ui.blog.contract.BlogPresenter
import com.shopapp.ui.blog.router.BlogRouter
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

    @Provides
    fun provideBlogRouter(): BlogRouter = BlogRouter()
}