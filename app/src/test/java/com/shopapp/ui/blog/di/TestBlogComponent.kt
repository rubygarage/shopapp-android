package com.shopapp.ui.blog.di

import com.shopapp.ui.blog.ArticleActivity
import com.shopapp.ui.blog.BlogActivity
import com.shopapp.ui.blog.BlogFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestBlogModule::class])
interface TestBlogComponent : BlogComponent {

    override fun inject(activity: BlogActivity)

    override fun inject(fragment: BlogFragment)

    override fun inject(articleActivity: ArticleActivity)
}