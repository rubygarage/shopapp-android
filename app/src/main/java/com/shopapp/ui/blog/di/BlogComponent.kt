package com.client.shop.ui.blog.di

import com.client.shop.ui.blog.ArticleActivity
import com.client.shop.ui.blog.BlogActivity
import com.client.shop.ui.blog.BlogFragment
import dagger.Subcomponent

@Subcomponent(modules = [BlogModule::class])
interface BlogComponent {

    fun inject(activity: BlogActivity)

    fun inject(fragment: BlogFragment)

    fun inject(articleActivity: ArticleActivity)
}