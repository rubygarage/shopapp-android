package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.contract.ArticlePresenter
import com.client.shop.ui.blog.contract.BlogPresenter
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides

@Module
class TestBlogModule {

    @Provides
    fun provideBlogPresenter(): BlogPresenter = mock()

    @Provides
    fun provideArticlePresenter(): ArticlePresenter = mock()
}