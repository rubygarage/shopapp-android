package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.ArticleActivity
import com.client.shop.ui.blog.BlogActivity
import com.client.shop.ui.blog.BlogFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestBlogModule::class])
interface TestBlogComponent : BlogComponent {

    override fun inject(activity: BlogActivity)

    override fun inject(fragment: BlogFragment)

    override fun inject(articleActivity: ArticleActivity)
}