package com.shopapp.ui.blog.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.blog.contract.ArticlePresenter
import com.shopapp.ui.blog.contract.BlogPresenter
import dagger.Module
import dagger.Provides

@Module
class TestBlogModule {

    @Provides
    fun provideBlogPresenter(): BlogPresenter = mock()

    @Provides
    fun provideArticlePresenter(): ArticlePresenter = mock()
}